package com.songtrybe.tv.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api

@Composable
fun rememberTvFocusRequester(): FocusRequester {
    return remember { FocusRequester() }
}

@Composable
fun Modifier.tvFocusable(
    focusRequester: FocusRequester? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null
): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1.0f,
        label = "focus_scale"
    )
    
    return this
        .scale(scale)
        .focusable()
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
            onFocusChanged?.invoke(focusState.isFocused)
        }
        .then(
            if (focusRequester != null) {
                Modifier.focusRequester(focusRequester)
            } else {
                Modifier
            }
        )
        .border(
            BorderStroke(
                width = if (isFocused) 3.dp else 0.dp,
                color = if (isFocused) Color.White else Color.Transparent
            )
        )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvFocusableCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.05f else 1.0f,
        label = "card_scale"
    )
    
    androidx.tv.material3.Card(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                onFocusChanged?.invoke(focusState.isFocused)
            }
            .border(
                BorderStroke(
                    width = if (isFocused) 2.dp else 0.dp,
                    color = if (isFocused) Color.White.copy(alpha = 0.8f) else Color.Transparent
                )
            )
    ) {
        content()
    }
}

@Composable
fun TvLazyRowWithFocus(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        content()
    }
}

data class FocusState(
    val rowIndex: Int = 0,
    val itemIndex: Int = 0
)

@Composable
fun rememberTvFocusState(): MutableState<FocusState> {
    return remember { mutableStateOf(FocusState()) }
}