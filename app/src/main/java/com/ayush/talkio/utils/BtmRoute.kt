package com.ayush.talkio.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Storm
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BtmRoute(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    data object AllChats : BtmRoute("chat", Icons.Filled.Chat, "Chats")
    data object Profile : BtmRoute("profile", Icons.Filled.Chat, "Profile")
    data object Stories : BtmRoute("story", Icons.Filled.Storm, "Stories")
    data object Requests : BtmRoute("request", Icons.Filled.Notifications, "Requests")
    data object Chat : BtmRoute("chat", Icons.Filled.Chat, "Chat")

}