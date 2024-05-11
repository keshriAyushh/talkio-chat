package com.ayush.talkio.data.model

data class User(
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val bio: String = "",
    val pfp: String = "",
    @JvmField
    val isOnline: Boolean = false,
    val lastSeen: Long = 0L,
    val fcmToken: String = "",
    val timestamp: Long = 0L
) {
    constructor() : this(
        userId = "",
        name = "",
        phone = "",
        bio = "",
        pfp = "",
        fcmToken = "",
        timestamp = 0L,
        lastSeen = 0L,
        isOnline = false
    )
}