package com.ayush.talkio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.data.model.Chat
import com.ayush.talkio.presentation.ui.theme.Cyan
import com.ayush.talkio.utils.TimeUtil
import com.ayush.talkio.utils.getOtherUserId

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatItem(
    userId: String = "",
    chat: Chat = Chat(
        chatroomId = "",
        lastMessage = "Hello",
        lastMessageSenderId = "abc",
        lastMessageSenderName = "Keshri",
        lastMessageTime = 2234234,
        unreadCount = 0,
        members = listOf("abc", "def"),
        lastMessageSeen = true,
        senderName = "",
        senderId = "",
        senderPfp = "",
        receiverId = "",
        receiverName = "",
        receiverPfp = ""
    )
) {
    //userId is the id of the currently logged in user, otherUserId is the id of the other user
    val otherUserId = getOtherUserId(chat.members, userId)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (chat.receiverId == userId) {
                //The receiver is the current user
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(chat.senderPfp)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp),
                    contentDescription = null
                )
                Space(width = 10.dp)
                Column {
                    MyText(
                        text = chat.senderName,
                        color = Color.Black,
                        fontSize = 16,
                        fontWeight = FontWeight.Bold,
                        font = R.font.bold
                    )
                    Space(height = 2.dp)
                }
            } else {
                //The sender is the current user
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(chat.receiverPfp)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp),
                    contentDescription = null
                )
                Space(width = 10.dp)
                Column {
                    MyText(
                        text = chat.receiverName,
                        color = Color.Black,
                        fontSize = 16,
                        fontWeight = FontWeight.Bold,
                        font = R.font.bold
                    )
                    Space(height = 2.dp)
                }
            }
            Row {
                if (chat.lastMessageSenderId == userId) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "seen_or_not",
                        tint = if (chat.lastMessageSeen) Cyan else Color.Gray
                    )
                }
                Space(
                    width = 2.dp
                )
                Text(
                    text = chat.lastMessage,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.regular)),
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            MyText(
                text = TimeUtil.getTimeAgo(chat.lastMessageTime)!!,
                color = Color.Gray,
                fontSize = 12,
                fontWeight = FontWeight.Bold,
                font = R.font.regular
            )
        }
    }
}