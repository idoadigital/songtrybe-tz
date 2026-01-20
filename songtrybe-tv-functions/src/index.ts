import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import { google } from 'googleapis';

admin.initializeApp();
const db = admin.firestore();
const youtube = google.youtube('v3');

// Constants
const YOUTUBE_CACHE_COLLECTION = 'youtubeVideoCache';
const CACHE_TTL_MS = 29 * 24 * 60 * 60 * 1000; // 29 days
const MAX_BATCH_SIZE = 50; // YouTube API max per request

interface YouTubeVideoCache {
  videoId: string;
  title: string;
  channelTitle: string;
  publishedAt: admin.firestore.Timestamp;
  durationIso: string;
  durationSeconds: number;
  thumbnails: {
    defaultUrl: string;
    mediumUrl: string;
    highUrl: string;
    maxresUrl: string;
  };
  lastFetchedAt: admin.firestore.Timestamp;
  stats?: {
    viewCount: number;
    likeCount: number;
  };
  statsFetchedAt?: admin.firestore.Timestamp;
}

/**
 * Parse ISO 8601 duration to seconds
 */
function parseDuration(duration: string): number {
  const match = duration.match(/PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?/);
  if (!match) return 0;
  
  const hours = parseInt(match[1] || '0');
  const minutes = parseInt(match[2] || '0');
  const seconds = parseInt(match[3] || '0');
  
  return hours * 3600 + minutes * 60 + seconds;
}

/**
 * Get best thumbnail URL from YouTube thumbnails
 */
function getBestThumbnailUrl(thumbnails: any): string {
  return thumbnails?.maxres?.url ||
         thumbnails?.high?.url ||
         thumbnails?.medium?.url ||
         thumbnails?.default?.url ||
         `https://img.youtube.com/vi/${thumbnails?.videoId || ''}/hqdefault.jpg`;
}

/**
 * Fetch video metadata from YouTube Data API
 */
async function fetchVideoMetadataFromYouTube(videoIds: string[]): Promise<Map<string, any>> {
  const apiKey = functions.config().youtube?.api_key || process.env.YOUTUBE_API_KEY;
  if (!apiKey) {
    throw new functions.https.HttpsError('failed-precondition', 'YouTube API key not configured');
  }

  const results = new Map<string, any>();
  
  // Process in batches of MAX_BATCH_SIZE
  for (let i = 0; i < videoIds.length; i += MAX_BATCH_SIZE) {
    const batch = videoIds.slice(i, i + MAX_BATCH_SIZE);
    
    try {
      const response = await youtube.videos.list({
        key: apiKey,
        part: ['snippet', 'contentDetails', 'statistics'],
        id: batch,
      });

      if (response.data.items) {
        for (const item of response.data.items) {
          if (item.id) {
            results.set(item.id, item);
          }
        }
      }
    } catch (error) {
      console.error('YouTube API error:', error);
      // Continue with partial results
    }
  }

  return results;
}

/**
 * Update cache with fresh video metadata
 */
async function updateCache(videoId: string, videoData: any): Promise<YouTubeVideoCache> {
  const snippet = videoData.snippet || {};
  const contentDetails = videoData.contentDetails || {};
  const statistics = videoData.statistics || {};
  const now = admin.firestore.Timestamp.now();

  const cacheData: YouTubeVideoCache = {
    videoId,
    title: snippet.title || 'Untitled Video',
    channelTitle: snippet.channelTitle || '',
    publishedAt: admin.firestore.Timestamp.fromDate(new Date(snippet.publishedAt || Date.now())),
    durationIso: contentDetails.duration || 'PT0S',
    durationSeconds: parseDuration(contentDetails.duration || 'PT0S'),
    thumbnails: {
      defaultUrl: snippet.thumbnails?.default?.url || '',
      mediumUrl: snippet.thumbnails?.medium?.url || '',
      highUrl: snippet.thumbnails?.high?.url || '',
      maxresUrl: snippet.thumbnails?.maxres?.url || '',
    },
    lastFetchedAt: now,
  };

  // Add statistics if available
  if (statistics.viewCount || statistics.likeCount) {
    cacheData.stats = {
      viewCount: parseInt(statistics.viewCount || '0'),
      likeCount: parseInt(statistics.likeCount || '0'),
    };
    cacheData.statsFetchedAt = now;
  }

  // Write to Firestore
  await db.collection(YOUTUBE_CACHE_COLLECTION)
    .doc(videoId)
    .set(cacheData);

  return cacheData;
}

/**
 * Main callable function to get YouTube video metadata
 */
export const getYouTubeVideosMetadata = functions.https.onCall(
  async (data, context) => {
    // Basic validation
    if (!data.videoIds || !Array.isArray(data.videoIds)) {
      throw new functions.https.HttpsError('invalid-argument', 'videoIds must be an array');
    }

    const videoIds = data.videoIds as string[];
    if (videoIds.length === 0) {
      return {};
    }

    if (videoIds.length > 100) {
      throw new functions.https.HttpsError('invalid-argument', 'Maximum 100 video IDs allowed');
    }

    // Optional: Require authentication
    // if (!context.auth) {
    //   throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated');
    // }

    // Rate limiting (optional)
    // You could implement per-user rate limiting here using Firestore

    const results: Record<string, any> = {};
    const needsRefresh: string[] = [];
    const now = Date.now();

    // Check cache for each video ID
    const cachePromises = videoIds.map(async (videoId) => {
      try {
        const cacheDoc = await db.collection(YOUTUBE_CACHE_COLLECTION)
          .doc(videoId)
          .get();

        if (cacheDoc.exists) {
          const cacheData = cacheDoc.data() as YouTubeVideoCache;
          const lastFetchedAt = cacheData.lastFetchedAt.toMillis();
          
          // Check if cache is still valid (within 29 days)
          if (now - lastFetchedAt < CACHE_TTL_MS) {
            // Return cached data
            results[videoId] = {
              title: cacheData.title,
              channelTitle: cacheData.channelTitle,
              publishedAt: cacheData.publishedAt.toMillis(),
              durationSeconds: cacheData.durationSeconds,
              bestThumbnailUrl: getBestThumbnailUrl(cacheData.thumbnails),
              viewCount: cacheData.stats?.viewCount,
              likeCount: cacheData.stats?.likeCount,
            };
          } else {
            // Cache expired, needs refresh
            needsRefresh.push(videoId);
          }
        } else {
          // Not in cache, needs fetch
          needsRefresh.push(videoId);
        }
      } catch (error) {
        console.error(`Error checking cache for ${videoId}:`, error);
        needsRefresh.push(videoId);
      }
    });

    await Promise.all(cachePromises);

    // Fetch fresh data for videos that need refresh
    if (needsRefresh.length > 0) {
      const freshData = await fetchVideoMetadataFromYouTube(needsRefresh);
      
      // Update cache and prepare results
      const updatePromises = needsRefresh.map(async (videoId) => {
        const videoData = freshData.get(videoId);
        if (videoData) {
          const cacheData = await updateCache(videoId, videoData);
          results[videoId] = {
            title: cacheData.title,
            channelTitle: cacheData.channelTitle,
            publishedAt: cacheData.publishedAt.toMillis(),
            durationSeconds: cacheData.durationSeconds,
            bestThumbnailUrl: getBestThumbnailUrl(cacheData.thumbnails),
            viewCount: cacheData.stats?.viewCount,
            likeCount: cacheData.stats?.likeCount,
          };
        } else {
          // Video not found or error - return minimal data
          results[videoId] = {
            title: 'Video',
            channelTitle: '',
            publishedAt: Date.now(),
            durationSeconds: 0,
            bestThumbnailUrl: `https://img.youtube.com/vi/${videoId}/hqdefault.jpg`,
          };
        }
      });

      await Promise.all(updatePromises);
    }

    return results;
  }
);

/**
 * Scheduled function to refresh popular videos
 * Runs daily at 2 AM
 */
export const refreshPopularYouTubeMetadata = functions.pubsub
  .schedule('0 2 * * *')
  .timeZone('America/New_York')
  .onRun(async (context) => {
    console.log('Starting scheduled YouTube metadata refresh');
    
    // Get videos that are close to expiring (last 5 days of TTL)
    const expiryThreshold = admin.firestore.Timestamp.fromMillis(
      Date.now() - (24 * 24 * 60 * 60 * 1000) // 24 days ago
    );

    try {
      const snapshot = await db.collection(YOUTUBE_CACHE_COLLECTION)
        .where('lastFetchedAt', '<', expiryThreshold)
        .limit(50) // Limit to conserve quota
        .get();

      if (snapshot.empty) {
        console.log('No videos need refresh');
        return null;
      }

      const videoIds = snapshot.docs.map(doc => doc.id);
      console.log(`Refreshing ${videoIds.length} videos`);

      const freshData = await fetchVideoMetadataFromYouTube(videoIds);
      
      // Update cache
      const updatePromises = videoIds.map(async (videoId) => {
        const videoData = freshData.get(videoId);
        if (videoData) {
          await updateCache(videoId, videoData);
        } else {
          // If video no longer exists, delete from cache
          await db.collection(YOUTUBE_CACHE_COLLECTION).doc(videoId).delete();
        }
      });

      await Promise.all(updatePromises);
      console.log('Scheduled refresh completed');
    } catch (error) {
      console.error('Error in scheduled refresh:', error);
    }

    return null;
  });

/**
 * Clean up expired cache entries
 * Runs weekly
 */
export const cleanupExpiredCache = functions.pubsub
  .schedule('0 3 * * 0')
  .timeZone('America/New_York')
  .onRun(async (context) => {
    console.log('Starting cache cleanup');
    
    const expiryTime = admin.firestore.Timestamp.fromMillis(
      Date.now() - CACHE_TTL_MS
    );

    try {
      const snapshot = await db.collection(YOUTUBE_CACHE_COLLECTION)
        .where('lastFetchedAt', '<', expiryTime)
        .limit(100)
        .get();

      if (snapshot.empty) {
        console.log('No expired cache entries');
        return null;
      }

      const batch = db.batch();
      snapshot.docs.forEach(doc => {
        batch.delete(doc.ref);
      });

      await batch.commit();
      console.log(`Deleted ${snapshot.size} expired cache entries`);
    } catch (error) {
      console.error('Error in cache cleanup:', error);
    }

    return null;
  });