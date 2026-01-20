package com.songtrybe.tv.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.songtrybe.tv.data.firebase.FirebaseSchema
import com.songtrybe.tv.data.mapper.MusicianMapper
import com.songtrybe.tv.data.model.Musician
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MusicianRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val musiciansCollection = firestore.collection(FirebaseSchema.COL_MUSICIANS)
    
    suspend fun getHighlightedMusicians(limit: Int = 20): List<Musician> {
        return try {
            val snapshot = musiciansCollection
                .whereEqualTo(FirebaseSchema.FIELD_IS_HIGHLIGHTED, true)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.documents.mapNotNull { MusicianMapper.fromDocument(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getTrendingMusicians(limit: Int = 30): List<Musician> {
        return try {
            val snapshot = musiciansCollection
                .orderBy(FirebaseSchema.FIELD_UPVOTE_COUNT, Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.documents.mapNotNull { MusicianMapper.fromDocument(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getVerifiedMusicians(limit: Int = 30): List<Musician> {
        return try {
            val snapshot = musiciansCollection
                .whereEqualTo(FirebaseSchema.FIELD_IS_VERIFIED, true)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.documents.mapNotNull { MusicianMapper.fromDocument(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getAllMusiciansForGenreGrouping(limit: Int = 300): List<Musician> {
        return try {
            val snapshot = musiciansCollection
                .orderBy(FirebaseSchema.FIELD_UPVOTE_COUNT, Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.documents.mapNotNull { MusicianMapper.fromDocument(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getMusiciansByGenre(genre: String, limit: Int = 30): List<Musician> {
        return try {
            val snapshot = musiciansCollection
                .whereEqualTo(FirebaseSchema.FIELD_GENRE, genre)
                .orderBy(FirebaseSchema.FIELD_UPVOTE_COUNT, Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.documents.mapNotNull { MusicianMapper.fromDocument(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getMusicianById(musicianId: String): Musician? {
        return try {
            val document = musiciansCollection
                .document(musicianId)
                .get()
                .await()
            
            MusicianMapper.fromDocument(document)
        } catch (e: Exception) {
            null
        }
    }
    
    fun observeMusician(musicianId: String): Flow<Musician?> = callbackFlow {
        val listener = musiciansCollection
            .document(musicianId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                } else if (snapshot != null) {
                    trySend(MusicianMapper.fromDocument(snapshot))
                }
            }
        
        awaitClose { listener.remove() }
    }
    
    suspend fun searchMusicians(query: String): List<Musician> {
        // For MVP, we'll fetch a larger set and filter locally
        // In production, consider using Algolia or Firebase Extensions for better search
        return try {
            val allMusicians = getAllMusiciansForGenreGrouping(500)
            val lowerQuery = query.lowercase()
            
            allMusicians.filter { musician ->
                musician.name.lowercase().contains(lowerQuery) ||
                musician.genre.lowercase().contains(lowerQuery)
            }.take(50)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getDistinctGenres(): List<String> {
        return try {
            val musicians = getAllMusiciansForGenreGrouping(500)
            musicians
                .map { it.genre }
                .filter { it.isNotEmpty() }
                .distinct()
                .sorted()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getDistinctCategories(): List<String> {
        return try {
            val musicians = getAllMusiciansForGenreGrouping(500)
            musicians
                .flatMap { it.categories }
                .filter { it.isNotEmpty() }
                .distinct()
                .sorted()
        } catch (e: Exception) {
            emptyList()
        }
    }
}