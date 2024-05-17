package com.ayush.talkio.presentation.screens.main.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.data.model.Chat
import com.ayush.talkio.presentation.components.ChatItem
import com.ayush.talkio.presentation.components.DialogScreen
import com.ayush.talkio.presentation.components.Loading
import com.ayush.talkio.presentation.components.Space
import com.ayush.talkio.presentation.ui.theme.Surface
import com.ayush.talkio.presentation.viewmodels.AllChatScreenViewModel
import com.ayush.talkio.utils.BtmRoute
import com.ayush.talkio.utils.LocalAppNavigator
import com.ayush.talkio.utils.LocalSnackbarState
import com.ayush.talkio.utils.Response
import kotlinx.coroutines.launch

@Composable
fun AllChatsScreen(
    viewModel: AllChatScreenViewModel = hiltViewModel()
) {

    val navigator = LocalAppNavigator.current

    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }

    val userId = rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllChats()
        viewModel.getFCMToken()
        userId.value = viewModel.getCurrentUserId()
    }

    val scope = rememberCoroutineScope()
    val snackbar = LocalSnackbarState.current

    val receiverId = rememberSaveable {
        mutableStateOf("")
    }

    if (showDialog.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DialogScreen(
                userId = receiverId,
                onDismiss = {
                    showDialog.value = false
                },
                onConfirm = { id ->
                    if (id.isEmpty()) {
                        scope.launch {
                            snackbar.showSnackbar(
                                message = "Please enter a valid user id!",
                                duration = SnackbarDuration.Short
                            )
                        }
                    } else {
                        receiverId.value = id
                        showDialog.value = false
                        viewModel.sendChatRequest(id)
                    }
                }
            )
        }
    }


    viewModel.chatRequestEvent.collectAsState().value.let {
        when (it) {
            is Response.Error -> {
                scope.launch {
                    snackbar.showSnackbar(
                        message = it.error,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            Response.Loading -> {}
            Response.None -> {}
            is Response.Success -> {
                if (it.data) {
                    scope.launch {
                        snackbar.showSnackbar(
                            message = "Chat request sent successfully!",
                            duration = SnackbarDuration.Short
                        )
                    }
                } else {
                    scope.launch {
                        snackbar.showSnackbar(
                            message = "Failed to send chat request. Please try again!",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = Surface,
                contentColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 10.dp
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    MyText(
                        text = "Talkio",
                        color = Color.Black,
                        fontSize = 22,
                        fontWeight = FontWeight.Bold,
                        font = R.font.bold
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        showDialog.value = true
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "send request")
                }
            }
            viewModel.allChatsEvent.collectAsState().value.let {
                when (it) {
                    is Response.Error -> {
                        scope.launch {
                            snackbar.showSnackbar(
                                message = it.error,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }

                    Response.Loading -> {
                        Loading()
                    }

                    Response.None -> {}
                    is Response.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(1f),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            items(it.data) { chatItem ->
                                ChatItem(
                                    userId = userId.value,
                                    chatItem
                                ) { chat: Chat ->
                                    if (userId.value == chat.senderId) {
                                        //Current user is the sender, hence open the receivers chat
                                        navigator.navigate("${BtmRoute.Chat.route}/${chat.receiverId}")
                                    } else {
                                        navigator.navigate("${BtmRoute.Chat.route}/${chat.senderId}")
                                    }
                                }
                                Space(height = 4.dp)
                            }
                        }
                    }
                }
            }
        }

    }
}