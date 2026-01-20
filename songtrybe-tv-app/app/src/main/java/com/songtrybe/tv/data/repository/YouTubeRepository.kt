package com.songtrybe.tv.data.repository

import com.google.firebase.functions.FirebaseFunctions
import com.songtrybe.tv.data.firebase.FirebaseSchema
import com.songtrybe.tv.data.model.YouTubeVideo
import kotlinx.coroutines.tasks.await

class YouTubeRepository {
    private val functions = FirebaseFunctions.getInstance()
    
    suspend fun getVideosMetadata(videoIds: List<String>): Map<String, YouTubeVideo> {
        if (videoIds.isEmpty()) return emptyMap()
        
        return try {
            val data = hashMapOf(
                "videoIds" to videoIds
            )
            
            val result = functions
                .getHttpsCallable(FirebaseSchema.FUNCTION_GET_YOUTUBE_METADATA)
                .call(data)
                .await()
            
            @Suppress("UNCHECKED_CAST")
            val responseData = result.data as? Map<String, Map<String, Any>> ?: emptyMap()
            
            responseData.mapValues { (videoId, videoData) ->
                YouTubeVideo(
                    videoId = videoId,
                    title = videoData["title"] as? String ?: "Video",
                    channelTitle = videoData["channelTitle"] as? String ?: "",
                    publishedAt = (videoData["publishedAt"] as? Long) ?: 0L,
                    durationSeconds = (videoData["durationSeconds"] as? Number)?.toInt() ?: 0,
                    thumbnailUrl = videoData["bestThumbnailUrl"] as? String ?: "",
                    viewCount = (videoData["viewCount"] as? Number)?.toLong(),
                    likeCount = (videoData["likeCount"] as? Number)?.toLong()
                )
            }
        } catch (e: Exception) {
            // Return empty map on error, could log to Crashlytics here
            emptyMap()
        }
    }
    
    suspend fun getVideoMetadata(videoId: String): YouTubeVideo? {
        val result = getVideosMetadata(listOf(videoId))
        return result[videoId]
    }
}