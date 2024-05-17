package com.ayush.talkio.presentation.screens.main.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.data.model.Message
import com.ayush.talkio.data.model.User
import com.ayush.talkio.presentation.components.MessageBubble
import com.ayush.talkio.presentation.components.MyTextField
import com.ayush.talkio.presentation.components.Space
import com.ayush.talkio.presentation.ui.theme.Cyan
import com.ayush.talkio.presentation.ui.theme.Surface
import com.ayush.talkio.presentation.viewmodels.ChatViewModel
import com.ayush.talkio.utils.LocalAppNavigator
import com.ayush.talkio.utils.LocalSnackbarState
import com.ayush.talkio.utils.Response
import com.ayush.talkio.utils.TimeUtil
import kotlinx.coroutines.launch


@Composable
fun ChatScreen(
    receiverId: String,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val message = rememberSaveable {
        mutableStateOf("")
    }

    val info = remember {
        mutableStateOf(User())
    }

    val navigator = LocalAppNavigator.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val snackbar = LocalSnackbarState.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllMessages(receiverId)
        viewModel.getReceiverInfo(receiverId)

    }
    LaunchedEffect(receiverId) {
        viewModel.startListeningForUserOnlineStatus(receiverId)
    }

    val isUserOnline by viewModel.isUserOnline.observeAsState(false)

    viewModel.userInfo.collectAsState().value.let {
        when (it) {
            is Response.Error -> {
                scope.launch {
                    snackbar.showSnackbar(
                        message = it.error,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }
            }

            Response.Loading -> {}
            Response.None -> {}
            is Response.Success -> info.value = it.data
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    navigator.popBackStack()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black,
                    containerColor = Color.Transparent
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "back button"
                )
            }
            Space(width = 2.dp)
            if (info.value.pfp.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(info.value.pfp)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp),
                    contentDescription = null
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Person",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp),
                    tint = Color.Black
                )
            }
            Space(width = 8.dp)
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                MyText(
                    text = info.value.name,
                    color = Color.Black,
                    fontSize = 18,
                    font = R.font.bold

                )
                Space(height = 2.dp)
                MyText(
                    text = if (isUserOnline) "Online" else TimeUtil.getTimeAgo(info.value.lastSeen)
                        ?.let { "last seen $it" } ?: "",
                    color = Color.Black,
                    fontSize = 12,
                    font = R.font.regular
                )

            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More options",
                    tint = Color.Black
                )
            }
        }
        Space(height = 10.dp)
        viewModel.allMessagesState.collectAsState().value.let {
            when (it) {
                is Response.Error -> {
                    scope.launch {
                        snackbar.showSnackbar(
                            message = it.error,
                            duration = SnackbarDuration.Short,
                            withDismissAction = true
                        )
                    }
                }

                Response.Loading -> {}
                Response.None -> {}
                is Response.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp),
                        state = listState
                    ) {
                        items(it.data) { message ->
                            Column(
                                horizontalAlignment = if (message.senderId != receiverId) Alignment.End else Alignment.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                MessageBubble(
                                    message = message,
                                    color = if (message.senderId == receiverId) Color.White else Cyan,
                                )
                                Space(height = 10.dp)
                            }
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "add_media",
                        tint = Color.Black
                    )
                }
                MyTextField(
                    text = message,
                    placeholder = "Message",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default
                    ),
                    maxLines = 500,
                    modifier = Modifier.weight(1f)
                ) { newMessage ->

                }
                IconButton(
                    onClick = {
                        if (message.value.isNotEmpty()) {
                            viewModel.sendMessage(
                                Message(
                                    receiverId = receiverId,
                                    text = message.value
                                )
                            )
                            message.value = ""
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "add_media",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}