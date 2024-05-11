package com.ayush.talkio.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun Space(
    height: Dp? = null,
    width: Dp? = null
) {
    height?.let {
        Spacer(
            modifier = Modifier.height(height)
        )
    }

    width?.let {
        Spacer(modifier = Modifier.width(width))
    }
}