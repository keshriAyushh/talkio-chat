package com.ayush.convoz.presentation.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.ayush.talkio.R

@Composable
fun MyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    fontSize: Int = 16,
    font: Int = R.font.regular,
    textAlign: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize.sp,
        fontFamily = FontFamily(Font(font)),
        textAlign = textAlign
    )
}