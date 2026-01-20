package com.songtrybe.tv.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Musician(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val genre: String = "",
    val location: String = "",
    val bio: String = "",
    val instagram: String = "",
    val tiktok: String = "",
    val youtubeChannel: String = "",
    val youtubeUrls: List<String> = emptyList(),
    val isHighlighted: Boolean = false,
    val isVerified: Boolean = false,
    val upvoteCount: Int = 0,
    val categories: List<String> = emptyList(),
    val territory: String = "",
    val monetize: Boolean = false,
    val rate: Double = 0.0,
    val appleMusic: String = "",
    val spotify: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",
    val managerName: String = "",
    val managerEmail: String = "",
    val pressKitUrl: String = "",
    val updatedAt: Long = 0L,
    val timestamp: Long = 0L
)