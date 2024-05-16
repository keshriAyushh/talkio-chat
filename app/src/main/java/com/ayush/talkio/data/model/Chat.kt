package com.ayush.talkio.data.model

data class Chat(
    val chatroomId: String = "",
    val members: List<String> = emptyList(),
    val senderId: String = "",
    val senderName: String = "",
    val senderPfp: String = "",
    val receiverId: String = "",
    val receiverName: String = "",
    val receiverPfp: String = "",
    val lastMessage: String = "",
    val lastMessageSenderId: String = "",
    val lastMessageSenderName: String = "",
    val lastMessageTime: Long = 0L,
    val unreadCount: Int = 0,
    val lastMessageSeen: Boolean = false,
    val messages: List<Message> = emptyList()
)
