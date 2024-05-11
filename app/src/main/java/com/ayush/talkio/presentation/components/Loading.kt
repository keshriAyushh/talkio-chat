package com.ayush.talkio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ayush.talkio.presentation.ui.theme.Cyan

@Composable
fun Loading(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.Transparent)
            .alpha(0.7f)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Cyan
        )
    }

}