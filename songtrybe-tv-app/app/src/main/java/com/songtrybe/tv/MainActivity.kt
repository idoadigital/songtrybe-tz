package com.songtrybe.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.songtrybe.tv.navigation.TvNavigation
import com.songtrybe.tv.ui.theme.SongtrybeTvTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase
        Firebase.initialize(this)
        
        setContent {
            SongtrybeTvTheme {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    TvNavigation()
                }
            }
        }
    }
}