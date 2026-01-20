package com.songtrybe.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import com.songtrybe.tv.ui.components.ContentRail

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreen(
    onMusicianClick: (String) -> Unit,
    onNavigateToDiscover: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onFocus = { selectedTabIndex = 0 },
                onClick = { /* Already on Home */ }
            ) {
                Text(
                    "Home",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                )
            }
            Tab(
                selected = selectedTabIndex == 1,
                onFocus = { selectedTabIndex = 1 },
                onClick = { onNavigateToDiscover() }
            ) {
                Text(
                    "Discover",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                )
            }
            Tab(
                selected = selectedTabIndex == 2,
                onFocus = { selectedTabIndex = 2 },
                onClick = { onNavigateToSearch() }
            ) {
                Text(
                    "Search",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Normal
                )
            }
            Tab(
                selected = selectedTabIndex == 3,
                onFocus = { selectedTabIndex = 3 },
                onClick = { onNavigateToLibrary() }
            ) {
                Text(
                    "Library",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    fontWeight = if (selectedTabIndex == 3) FontWeight.Bold else FontWeight.Normal
                )
            }
            Tab(
                selected = selectedTabIndex == 4,
                onFocus = { selectedTabIndex = 4 },
                onClick = { onNavigateToSettings() }
            ) {
                Text(
                    "Settings",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    fontWeight = if (selectedTabIndex == 4) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
        
        // Content
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            
            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    if (uiState.highlighted.isNotEmpty()) {
                        item(key = "highlighted") {
                            ContentRail(
                                title = "Highlighted Artists",
                                musicians = uiState.highlighted,
                                onMusicianClick = onMusicianClick
                            )
                        }
                    }
                    
                    if (uiState.trending.isNotEmpty()) {
                        item(key = "trending") {
                            ContentRail(
                                title = "Trending Now",
                                musicians = uiState.trending,
                                onMusicianClick = onMusicianClick,
                                isCompact = true
                            )
                        }
                    }
                    
                    if (uiState.verified.isNotEmpty()) {
                        item(key = "verified") {
                            ContentRail(
                                title = "Verified Artists",
                                musicians = uiState.verified,
                                onMusicianClick = onMusicianClick,
                                isCompact = true
                            )
                        }
                    }
                    
                    uiState.genreRails.forEach { (genre, musicians) ->
                        if (musicians.isNotEmpty()) {
                            item(key = "genre_$genre") {
                                ContentRail(
                                    title = genre,
                                    musicians = musicians,
                                    onMusicianClick = onMusicianClick,
                                    isCompact = true
                                )
                            }
                        }
                    }
                }
            }
            
            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Something went wrong",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.tv.material3.Button(
                            onClick = { viewModel.retry() }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}