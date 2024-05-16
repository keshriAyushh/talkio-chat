package com.ayush.talkio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.data.model.ChatRequest

@Composable
fun RequestItem(
    request: ChatRequest = ChatRequest(
        senderPfp = "https://media.licdn.com/dms/image/D4D03AQHMGQ3l4ujIyg/profile-displayphoto-shrink_200_200/0/1669912620728?e=1721260800&v=beta&t=eUtCwweElsFHcZapcSXHa15Q8JFSBNiLI7lGZ4cZbDo",
        senderName = "Ayush",
        from = "",
        timestamp = 34423432323L,
        to = ""
    ),
    onReject: (ChatRequest) -> Unit,
    onAccept: (ChatRequest) -> Unit
) {
    Card(
        modifier = Modifier.wrapContentSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (request.senderPfp.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(request.senderPfp)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp),
                    contentDescription = null
                )
            }
            Space(width = 10.dp)
            MyText(
                text = request.senderName,
                color = Color.Black,
                fontSize = 16,
                fontWeight = FontWeight.Bold,
                font = R.font.bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        onReject(request)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Red
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "decline"
                    )
                }
                Space(width = 4.dp)
                IconButton(
                    onClick = {
                        onAccept(request)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Blue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "accept"
                    )
                }
            }
        }
    }
}