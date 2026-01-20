package com.songtrybe.tv.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    val musicianId: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class RecentlyPlayed(
    val id: String = "",
    val videoId: String,
    val musicianId: String,
    val youtubeUrl: String,
    val playedAt: Long = System.currentTimeMillis()
)