package com.songtrybe.tv.data.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.songtrybe.tv.data.firebase.FirebaseSchema
import com.songtrybe.tv.data.model.Musician

object MusicianMapper {
    
    fun fromDocument(document: DocumentSnapshot): Musician? {
        return try {
            val data = document.data ?: return null
            
            Musician(
                id = document.id,
                name = data[FirebaseSchema.FIELD_NAME]?.toString() ?: "",
                imageUrl = data[FirebaseSchema.FIELD_IMAGE_URL]?.toString() ?: "",
                genre = data[FirebaseSchema.FIELD_GENRE]?.toString() ?: "",
                location = data[FirebaseSchema.FIELD_LOCATION]?.toString() ?: "",
                bio = data[FirebaseSchema.FIELD_BIO]?.toString() ?: "",
                instagram = data[FirebaseSchema.FIELD_INSTAGRAM]?.toString() ?: "",
                tiktok = data[FirebaseSchema.FIELD_TIKTOK]?.toString() ?: "",
                youtubeChannel = data[FirebaseSchema.FIELD_YOUTUBE_CHANNEL]?.toString() ?: "",
                youtubeUrls = extractStringList(data[FirebaseSchema.FIELD_YOUTUBE_URLS]),
                isHighlighted = data[FirebaseSchema.FIELD_IS_HIGHLIGHTED] as? Boolean ?: false,
                isVerified = data[FirebaseSchema.FIELD_IS_VERIFIED] as? Boolean ?: false,
                upvoteCount = extractNumber(data[FirebaseSchema.FIELD_UPVOTE_COUNT]).toInt(),
                categories = extractStringList(data[FirebaseSchema.FIELD_CATEGORIES]),
                territory = data[FirebaseSchema.FIELD_TERRITORY]?.toString() ?: "",
                monetize = data[FirebaseSchema.FIELD_MONETIZE] as? Boolean ?: false,
                rate = extractNumber(data[FirebaseSchema.FIELD_RATE]).toDouble(),
                appleMusic = data[FirebaseSchema.FIELD_APPLE_MUSIC]?.toString() ?: "",
                spotify = data[FirebaseSchema.FIELD_SPOTIFY]?.toString() ?: "",
                contactEmail = data[FirebaseSchema.FIELD_CONTACT_EMAIL]?.toString() ?: "",
                contactPhone = data[FirebaseSchema.FIELD_CONTACT_PHONE]?.toString() ?: "",
                managerName = data[FirebaseSchema.FIELD_MANAGER_NAME]?.toString() ?: "",
                managerEmail = data[FirebaseSchema.FIELD_MANAGER_EMAIL]?.toString() ?: "",
                pressKitUrl = data[FirebaseSchema.FIELD_PRESS_KIT_URL]?.toString() ?: "",
                updatedAt = extractTimestamp(data[FirebaseSchema.FIELD_UPDATED_AT]),
                timestamp = extractTimestamp(data[FirebaseSchema.FIELD_TIMESTAMP])
            )
        } catch (e: Exception) {
            null
        }
    }
    
    private fun extractStringList(value: Any?): List<String> {
        return when (value) {
            is List<*> -> value.mapNotNull { item ->
                when (item) {
                    is String -> item
                    else -> item?.toString()
                }
            }
            else -> emptyList()
        }
    }
    
    private fun extractNumber(value: Any?): Long {
        return when (value) {
            is Long -> value
            is Int -> value.toLong()
            is Double -> value.toLong()
            is Float -> value.toLong()
            is String -> value.toLongOrNull() ?: 0L
            else -> 0L
        }
    }
    
    private fun extractTimestamp(value: Any?): Long {
        return when (value) {
            is Timestamp -> value.toDate().time
            is Long -> value
            is Double -> value.toLong()
            else -> 0L
        }
    }
}