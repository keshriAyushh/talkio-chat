package com.ayush.talkio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.data.model.Message
import com.ayush.talkio.presentation.ui.theme.Cyan
import java.util.Date

@Composable
fun MessageBubble(
    message: Message,
    color: Color = Cyan,
) {
    val showTime = rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                showTime.value = !showTime.value
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = color,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            MyText(
                text = message.text,
                color = Color.Black,
                fontSize = 16,
                font = R.font.regular
            )
            if (showTime.value) {
                Space(height = 2.dp)
                MyText(
                    text = Date(message.timestamp).toString().substring(11, 16),
                    color = Color.Black,
                    fontSize = 12,
                    font = R.font.regular
                )
            }
        }
    }
}
