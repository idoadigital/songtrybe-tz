package com.songtrybe.tv.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.songtrybe.tv.data.model.Musician
import com.songtrybe.tv.data.model.YouTubeVideo
import com.songtrybe.tv.data.repository.MusicianRepository
import com.songtrybe.tv.data.repository.UserDataRepository
import com.songtrybe.tv.data.repository.YouTubeRepository
import com.songtrybe.tv.util.youtube.YouTubeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MusicianDetailUiState {
    object Loading : MusicianDetailUiState()
    data class Success(
        val musician: Musician,
        val videos: Map<String, YouTubeVideo>,
        val isFavorite: Boolean
    ) : MusicianDetailUiState()
    data class Error(val message: String) : MusicianDetailUiState()
}

class MusicianDetailViewModel : ViewModel() {
    private val musicianRepository = MusicianRepository()
    private val youTubeRepository = YouTubeRepository()
    private val userDataRepository = UserDataRepository()
    
    private val _uiState = MutableStateFlow<MusicianDetailUiState>(MusicianDetailUiState.Loading)
    val uiState: StateFlow<MusicianDetailUiState> = _uiState.asStateFlow()
    
    private var currentMusicianId: String? = null
    
    fun loadMusician(musicianId: String) {
        currentMusicianId = musicianId
        viewModelScope.launch {
            _uiState.value = MusicianDetailUiState.Loading
            
            try {
                val musician = musicianRepository.getMusicianById(musicianId)
                if (musician == null) {
                    _uiState.value = MusicianDetailUiState.Error("Musician not found")
                    return@launch
                }
                
                // Extract video IDs from YouTube URLs
                val videoIds = musician.youtubeUrls
                    .mapNotNull { YouTubeUtils.extractYouTubeId(it) }
                    .distinct()
                
                // Fetch video metadata (will use cloud function)
                val videosMetadata = if (videoIds.isNotEmpty()) {
                    youTubeRepository.getVideosMetadata(videoIds)
                } else {
                    emptyMap()
                }
                
                // Check if musician is favorited
                val isFavorite = userDataRepository.isFavorite(musicianId)
                
                _uiState.value = MusicianDetailUiState.Success(
                    musician = musician,
                    videos = videosMetadata,
                    isFavorite = isFavorite
                )
            } catch (e: Exception) {
                _uiState.value = MusicianDetailUiState.Error(
                    e.message ?: "Failed to load musician"
                )
            }
        }
    }
    
    fun retry() {
        currentMusicianId?.let { loadMusician(it) }
    }
    
    fun addFavorite() {
        val currentState = _uiState.value as? MusicianDetailUiState.Success ?: return
        
        viewModelScope.launch {
            if (userDataRepository.addFavorite(currentState.musician.id)) {
                _uiState.value = currentState.copy(isFavorite = true)
            }
        }
    }
    
    fun removeFavorite() {
        val currentState = _uiState.value as? MusicianDetailUiState.Success ?: return
        
        viewModelScope.launch {
            if (userDataRepository.removeFavorite(currentState.musician.id)) {
                _uiState.value = currentState.copy(isFavorite = false)
            }
        }
    }
    
    fun logRecentlyPlayed(videoId: String, youtubeUrl: String) {
        val currentState = _uiState.value as? MusicianDetailUiState.Success ?: return
        
        viewModelScope.launch {
            userDataRepository.addRecentlyPlayed(
                videoId = videoId,
                musicianId = currentState.musician.id,
                youtubeUrl = youtubeUrl
            )
        }
    }
}