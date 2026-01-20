package com.songtrybe.tv.util.youtube

import android.content.Context
import android.content.Intent
import android.net.Uri

object YouTubeUtils {
    
    private val YOUTUBE_URL_PATTERNS = listOf(
        Regex("(?:https?://)?(?:www\\.)?youtube\\.com/watch\\?v=([a-zA-Z0-9_-]{11})"),
        Regex("(?:https?://)?(?:www\\.)?youtu\\.be/([a-zA-Z0-9_-]{11})"),
        Regex("(?:https?://)?(?:www\\.)?youtube\\.com/shorts/([a-zA-Z0-9_-]{11})"),
        Regex("(?:https?://)?(?:www\\.)?youtube\\.com/embed/([a-zA-Z0-9_-]{11})"),
        Regex("(?:https?://)?(?:www\\.)?m\\.youtube\\.com/watch\\?v=([a-zA-Z0-9_-]{11})")
    )
    
    fun extractYouTubeId(url: String): String? {
        for (pattern in YOUTUBE_URL_PATTERNS) {
            val match = pattern.find(url)
            if (match != null) {
                return match.groupValues[1]
            }
        }
        return null
    }
    
    fun getThumbnailUrl(videoId: String, quality: ThumbnailQuality = ThumbnailQuality.MAXRES): String {
        return when (quality) {
            ThumbnailQuality.DEFAULT -> "https://img.youtube.com/vi/$videoId/default.jpg"
            ThumbnailQuality.MEDIUM -> "https://img.youtube.com/vi/$videoId/mqdefault.jpg"
            ThumbnailQuality.HIGH -> "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
            ThumbnailQuality.STANDARD -> "https://img.youtube.com/vi/$videoId/sddefault.jpg"
            ThumbnailQuality.MAXRES -> "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"
        }
    }
    
    fun getBestThumbnailUrl(videoId: String): String {
        return getThumbnailUrl(videoId, ThumbnailQuality.MAXRES)
    }
    
    fun getFallbackThumbnailUrl(videoId: String): String {
        return getThumbnailUrl(videoId, ThumbnailQuality.HIGH)
    }
    
    fun launchYouTubeVideo(context: Context, videoId: String): Boolean {
        return try {
            // Try YouTube app first
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
            appIntent.putExtra("VIDEO_ID", videoId)
            appIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            
            if (appIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(appIntent)
                true
            } else {
                // Fallback to web URL
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
                webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(webIntent)
                true
            }
        } catch (e: Exception) {
            false
        }
    }
    
    fun formatDuration(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        
        return when {
            hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, secs)
            else -> String.format("%d:%02d", minutes, secs)
        }
    }
    
    fun parseDurationIso8601(duration: String): Int {
        // Parse ISO 8601 duration format (e.g., "PT3M12S")
        val regex = Regex("PT(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+)S)?")
        val match = regex.find(duration) ?: return 0
        
        val hours = match.groupValues[1].toIntOrNull() ?: 0
        val minutes = match.groupValues[2].toIntOrNull() ?: 0
        val seconds = match.groupValues[3].toIntOrNull() ?: 0
        
        return hours * 3600 + minutes * 60 + seconds
    }
    
    enum class ThumbnailQuality {
        DEFAULT,    // 120x90
        MEDIUM,     // 320x180
        HIGH,       // 480x360
        STANDARD,   // 640x480
        MAXRES      // 1280x720
    }
}