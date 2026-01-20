package com.songtrybe.tv.data.model

import kotlinx.serialization.Serializable

@Serializable
data class YouTubeVideo(
    val videoId: String,
    val title: String,
    val channelTitle: String = "",
    val publishedAt: Long = 0L,
    val durationSeconds: Int = 0,
    val thumbnailUrl: String = "",
    val viewCount: Long? = null,
    val likeCount: Long? = null
)

@Serializable
data class YouTubeVideoCache(
    val videoId: String,
    val title: String,
    val channelTitle: String,
    val publishedAt: Long,
    val durationIso: String,
    val durationSeconds: Int,
    val thumbnails: YouTubeThumbnails,
    val lastFetchedAt: Long,
    val stats: VideoStats? = null,
    val statsFetchedAt: Long? = null
)

@Serializable
data class YouTubeThumbnails(
    val defaultUrl: String = "",
    val mediumUrl: String = "",
    val highUrl: String = "",
    val maxresUrl: String = ""
) {
    val bestUrl: String
        get() = maxresUrl.ifEmpty { 
            highUrl.ifEmpty { 
                mediumUrl.ifEmpty { 
                    defaultUrl 
                }
            }
        }
}

@Serializable
data class VideoStats(
    val viewCount: Long = 0,
    val likeCount: Long = 0
)