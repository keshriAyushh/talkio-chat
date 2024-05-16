package com.ayush.talkio.data.model

data class ChatRequest(
    val requestId: String = "",
    val to: String = "",
    val from: String = "",
    val timestamp: Long = 0L,
    val senderName: String = "",
    val senderPfp: String = ""
)