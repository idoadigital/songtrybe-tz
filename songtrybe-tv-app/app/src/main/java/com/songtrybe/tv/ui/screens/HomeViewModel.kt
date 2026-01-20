package com.songtrybe.tv.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.songtrybe.tv.data.model.Musician
import com.songtrybe.tv.data.repository.MusicianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val highlighted: List<Musician>,
        val trending: List<Musician>,
        val verified: List<Musician>,
        val genreRails: Map<String, List<Musician>>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {
    private val repository = MusicianRepository()
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadContent()
    }
    
    private fun loadContent() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            
            try {
                val highlighted = repository.getHighlightedMusicians(20)
                val trending = repository.getTrendingMusicians(30)
                val verified = repository.getVerifiedMusicians(30)
                val allMusicians = repository.getAllMusiciansForGenreGrouping(300)
                
                // Group by genre and take top 10 from each genre
                val genreRails = allMusicians
                    .groupBy { it.genre }
                    .filterKeys { it.isNotEmpty() }
                    .mapValues { (_, musicians) -> musicians.take(10) }
                    .toList()
                    .sortedByDescending { (_, musicians) -> musicians.size }
                    .take(5) // Show top 5 genres as rails
                    .toMap()
                
                _uiState.value = HomeUiState.Success(
                    highlighted = highlighted,
                    trending = trending,
                    verified = verified,
                    genreRails = genreRails
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    fun retry() {
        loadContent()
    }
}