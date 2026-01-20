package com.songtrybe.tv.data.firebase

object FirebaseSchema {
    // Collections
    const val COL_MUSICIANS = "musicians"
    const val COL_YOUTUBE_VIDEO_CACHE = "youtubeVideoCache"
    const val COL_USERS = "users"
    const val COL_FAVORITES = "favorites"
    const val COL_RECENTLY_PLAYED = "recentlyPlayed"
    
    // Musician fields
    const val FIELD_NAME = "name"
    const val FIELD_IMAGE_URL = "imageUrl"
    const val FIELD_GENRE = "genre"
    const val FIELD_LOCATION = "location"
    const val FIELD_BIO = "bio"
    const val FIELD_INSTAGRAM = "instagram"
    const val FIELD_TIKTOK = "tiktok"
    const val FIELD_YOUTUBE_CHANNEL = "youtubeChannel"
    const val FIELD_YOUTUBE_URLS = "youtubeUrls"
    const val FIELD_IS_HIGHLIGHTED = "isHighlighted"
    const val FIELD_IS_VERIFIED = "isVerified"
    const val FIELD_UPVOTE_COUNT = "upvoteCount"
    const val FIELD_CATEGORIES = "categories"
    const val FIELD_UPDATED_AT = "updatedAt"
    const val FIELD_TIMESTAMP = "timestamp"
    const val FIELD_TERRITORY = "territory"
    const val FIELD_MONETIZE = "monetize"
    const val FIELD_RATE = "rate"
    const val FIELD_APPLE_MUSIC = "appleMusic"
    const val FIELD_SPOTIFY = "spotify"
    const val FIELD_CONTACT_EMAIL = "contactEmail"
    const val FIELD_CONTACT_PHONE = "contactPhone"
    const val FIELD_MANAGER_NAME = "managerName"
    const val FIELD_MANAGER_EMAIL = "managerEmail"
    const val FIELD_PRESS_KIT_URL = "pressKitUrl"
    
    // YouTube Video Cache fields
    const val FIELD_VIDEO_ID = "videoId"
    const val FIELD_TITLE = "title"
    const val FIELD_CHANNEL_TITLE = "channelTitle"
    const val FIELD_PUBLISHED_AT = "publishedAt"
    const val FIELD_DURATION_ISO = "durationIso"
    const val FIELD_DURATION_SECONDS = "durationSeconds"
    const val FIELD_THUMBNAILS = "thumbnails"
    const val FIELD_LAST_FETCHED_AT = "lastFetchedAt"
    const val FIELD_STATS = "stats"
    const val FIELD_STATS_FETCHED_AT = "statsFetchedAt"
    
    // User data fields
    const val FIELD_MUSICIAN_ID = "musicianId"
    const val FIELD_CREATED_AT = "createdAt"
    const val FIELD_PLAYED_AT = "playedAt"
    
    // Cloud Functions
    const val FUNCTION_GET_YOUTUBE_METADATA = "getYouTubeVideosMetadata"
    
    // Cache TTL (29 days in milliseconds)
    const val YOUTUBE_CACHE_TTL_MS = 29L * 24 * 60 * 60 * 1000
}