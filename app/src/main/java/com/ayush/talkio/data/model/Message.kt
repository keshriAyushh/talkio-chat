package com.ayush.talkio.data.model

data class Message(
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val receiverId: String = "",
    val receiverName: String = "",
    val timestamp: Long = 0L,
)
