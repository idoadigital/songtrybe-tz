package com.songtrybe.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import com.songtrybe.tv.data.model.YouTubeVideo
import com.songtrybe.tv.ui.components.TvFocusableCard
import com.songtrybe.tv.util.youtube.YouTubeUtils

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MusicianDetailScreen(
    musicianId: String,
    onBackClick: () -> Unit,
    viewModel: MusicianDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(musicianId) {
        viewModel.loadMusician(musicianId)
    }
    
    when (uiState) {
        is MusicianDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        
        is MusicianDetailUiState.Success -> {
            val musician = uiState.musician
            val videos = uiState.videos
            val isFavorite = uiState.isFavorite
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Hero Section
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        AsyncImage(
                            model = musician.imageUrl,
                            contentDescription = musician.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.9f)
                                        ),
                                        startY = 200f
                                    )
                                )
                        )
                        
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(48.dp)
                        ) {
                            Text(
                                text = musician.name,
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                if (musician.genre.isNotEmpty()) {
                                    Text(
                                        text = musician.genre,
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 18.sp
                                    )
                                }
                                if (musician.location.isNotEmpty()) {
                                    Text(
                                        text = "• ${musician.location}",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 18.sp
                                    )
                                }
                            }
                            
                            // Action Buttons
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(top = 24.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (isFavorite) {
                                            viewModel.removeFavorite()
                                        } else {
                                            viewModel.addFavorite()
                                        }
                                    }
                                ) {
                                    Text(
                                        text = if (isFavorite) "★ Favorited" else "☆ Add to Favorites",
                                        fontSize = 16.sp
                                    )
                                }
                                
                                Button(onClick = onBackClick) {
                                    Text("Back", fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
                
                // Bio Section
                if (musician.bio.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp)
                        ) {
                            Text(
                                text = "About",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = musician.bio,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }
                
                // Videos Section
                if (musician.youtubeUrls.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "Music Videos",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Plays via YouTube",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 14.sp
                                )
                            }
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                items(musician.youtubeUrls) { youtubeUrl ->
                                    val videoId = YouTubeUtils.extractYouTubeId(youtubeUrl)
                                    val video = videos[videoId]
                                    
                                    if (videoId != null) {
                                        VideoCard(
                                            videoId = videoId,
                                            video = video,
                                            onClick = {
                                                viewModel.logRecentlyPlayed(videoId, youtubeUrl)
                                                YouTubeUtils.launchYouTubeVideo(context, videoId)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Social Links
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = "Find on",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            if (musician.youtubeChannel.isNotEmpty()) {
                                SocialButton(
                                    text = "YouTube",
                                    onClick = { /* Open YouTube channel */ }
                                )
                            }
                            if (musician.instagram.isNotEmpty()) {
                                SocialButton(
                                    text = "Instagram",
                                    onClick = { /* Open Instagram */ }
                                )
                            }
                            if (musician.spotify.isNotEmpty()) {
                                SocialButton(
                                    text = "Spotify",
                                    onClick = { /* Open Spotify */ }
                                )
                            }
                            if (musician.appleMusic.isNotEmpty()) {
                                SocialButton(
                                    text = "Apple Music",
                                    onClick = { /* Open Apple Music */ }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        is MusicianDetailUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Failed to load artist",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.retry() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VideoCard(
    videoId: String,
    video: YouTubeVideo?,
    onClick: () -> Unit
) {
    TvFocusableCard(
        onClick = onClick,
        modifier = Modifier
            .width(320.dp)
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = video?.thumbnailUrl ?: YouTubeUtils.getBestThumbnailUrl(videoId),
                contentDescription = video?.title ?: "Video",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 100f
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = video?.title ?: "Video ${videoId.take(4)}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (video != null && video.durationSeconds > 0) {
                    Text(
                        text = YouTubeUtils.formatDuration(video.durationSeconds),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            // Play Icon
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "▶",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SocialButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(40.dp)
    ) {
        Text(text = text, fontSize = 14.sp)
    }
}