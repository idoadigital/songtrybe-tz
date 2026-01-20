package com.songtrybe.tv.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.songtrybe.tv.data.model.Musician

@Composable
fun ContentRail(
    title: String,
    musicians: List<Musician>,
    onMusicianClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 48.dp, vertical = 8.dp)
        )
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = musicians,
                key = { it.id }
            ) { musician ->
                if (isCompact) {
                    CompactMusicianCard(
                        musician = musician,
                        onClick = { onMusicianClick(musician.id) }
                    )
                } else {
                    MusicianCard(
                        musician = musician,
                        onClick = { onMusicianClick(musician.id) }
                    )
                }
            }
        }
    }
}