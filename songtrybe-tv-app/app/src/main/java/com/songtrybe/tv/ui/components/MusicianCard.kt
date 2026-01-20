package com.songtrybe.tv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import com.songtrybe.tv.data.model.Musician

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MusicianCard(
    musician: Musician,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Int = 200,
    height: Int = 280
) {
    TvFocusableCard(
        onClick = onClick,
        modifier = modifier
            .width(width.dp)
            .height(height.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
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
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 100f
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = musician.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (musician.genre.isNotEmpty()) {
                    Text(
                        text = musician.genre,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (musician.isVerified) {
                        VerifiedBadge()
                    }
                    if (musician.isHighlighted) {
                        HighlightedBadge()
                    }
                }
            }
        }
    }
}

@Composable
fun CompactMusicianCard(
    musician: Musician,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TvFocusableCard(
        onClick = onClick,
        modifier = modifier
            .width(160.dp)
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp))
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
                                Color.Black.copy(alpha = 0.6f)
                            ),
                            startY = 80f
                        )
                    )
            )
            
            Text(
                text = musician.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun VerifiedBadge() {
    Box(
        modifier = Modifier
            .background(Color(0xFF1DA1F2), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "✓",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HighlightedBadge() {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFD700), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "★",
            color = Color.Black,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}