package com.songtrybe.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// These are placeholder screens that need full implementation

@Composable
fun DiscoverScreen(
    onGenreClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Discover Screen - To Be Implemented",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun SearchScreen(
    onMusicianClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Search Screen - To Be Implemented",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun LibraryScreen(
    onMusicianClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Library Screen - To Be Implemented",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Settings Screen - To Be Implemented",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun GenreListScreen(
    genre: String,
    onMusicianClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Genre: $genre - To Be Implemented",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun CategoryListScreen(
    category: String,
    onMusicianClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Category: $category - To Be Implemented",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}