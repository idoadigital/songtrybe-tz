package com.songtrybe.tv.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.songtrybe.tv.data.firebase.FirebaseSchema
import com.songtrybe.tv.data.model.Favorite
import com.songtrybe.tv.data.model.RecentlyPlayed
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserDataRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    private val currentUserId: String?
        get() = auth.currentUser?.uid
    
    // Favorites
    suspend fun addFavorite(musicianId: String): Boolean {
        val userId = currentUserId ?: return false
        
        return try {
            val favorite = Favorite(
                musicianId = musicianId,
                createdAt = System.currentTimeMillis()
            )
            
            firestore.collection(FirebaseSchema.COL_USERS)
                .document(userId)
                .collection(FirebaseSchema.COL_FAVORITES)
                .document(musicianId)
                .set(favorite)
                .await()
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun removeFavorite(musicianId: String): Boolean {
        val userId = currentUserId ?: return false
        
        return try {
            firestore.collection(FirebaseSchema.COL_USERS)
                .document(userId)
                .collection(FirebaseSchema.COL_FAVORITES)
                .document(musicianId)
                .delete()
                .await()
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun isFavorite(musicianId: String): Boolean {
        val userId = currentUserId ?: return false
        
        return try {
            val doc = firestore.collection(FirebaseSchema.COL_USERS)
                .document(userId)
                .collection(FirebaseSchema.COL_FAVORITES)
                .document(musicianId)
                .get()
                .await()
            
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }
    
    fun observeFavorites(): Flow<List<String>> = callbackFlow {
        val userId = currentUserId
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        
        val listener = firestore.collection(FirebaseSchema.COL_USERS)
            .document(userId)
            .collection(FirebaseSchema.COL_FAVORITES)
            .orderBy(FirebaseSchema.FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                } else {
                    val favoriteIds = snapshot.documents.map { it.id }
                    trySend(favoriteIds)
                }
            }
        
        awaitClose { listener.remove() }
    }
    
    suspend fun getFavorites(): List<String> {
        val userId = currentUserId ?: return emptyList()
        
        return try {
            val snapshot = firestore.collection(FirebaseSchema.COL_USERS)
                .document(userId)
                .collection(FirebaseSchema.COL_FAVORITES)
                .orderBy(FirebaseSchema.FIELD_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()
            
            snapshot.documents.map { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Recently Played
    suspend fun addRecentlyPlayed(
        videoId: String,
        musicianId: String,
        youtubeUrl: String
    ): Boolean {
        val userId = currentUserId ?: return false
        
        return try {
            val recentlyPlayed = RecentlyPlayed(
                videoId = videoId,
                musicianId = musicianId,
                youtubeUrl = youtubeUrl,
                playedAt = System.currentTimeMillis()
            )
            
            firestore.collection(FirebaseSchema.COL_USERS)
                .document(userId)
                .collection(FirebaseSchema.COL_RECENTLY_PLAYED)
                .add(recentlyPlayed)
                .await()
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun getRecentlyPlayed(limit: Int = 20): List<RecentlyPlayed> {
        val userId = currentUserId ?: return emptyList()
        
        return try {
            val snapshot = firestore.collection(FirebaseSchema.COL_USERS)
                .document(userId)
                .collection(FirebaseSchema.COL_RECENTLY_PLAYED)
                .orderBy(FirebaseSchema.FIELD_PLAYED_AT, Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(RecentlyPlayed::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun observeRecentlyPlayed(limit: Int = 20): Flow<List<RecentlyPlayed>> = callbackFlow {
        val userId = currentUserId
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        
        val listener = firestore.collection(FirebaseSchema.COL_USERS)
            .document(userId)
            .collection(FirebaseSchema.COL_RECENTLY_PLAYED)
            .orderBy(FirebaseSchema.FIELD_PLAYED_AT, Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                } else {
                    val items = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(RecentlyPlayed::class.java)?.copy(id = doc.id)
                    }
                    trySend(items)
                }
            }
        
        awaitClose { listener.remove() }
    }
    
    // Delete user data
    suspend fun deleteAllUserData(): Boolean {
        val userId = currentUserId ?: return false
        
        return try {
            // Delete favorites
            val favoritesSnapshot = firestore.collection(FirebaseSchema.COL_USERS)
                .document(userId)
                .collection(FirebaseSchema.COL_FAVORITES)
                .get()
                .await()
            
            for (doc in favoritesSnapshot.documents) {
                doc.reference.delete().await()
            }
            
            // Delete recently played
            val recentSnapshot = firestore.collection(FirebaseSchema.COL_USERS)
                .document(userId)
                .collection(FirebaseSchema.COL_RECENTLY_PLAYED)
                .get()
                .await()
            
            for (doc in recentSnapshot.documents) {
                doc.reference.delete().await()
            }
            
            true
        } catch (e: Exception) {
            false
        }
    }
}