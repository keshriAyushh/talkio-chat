package com.ayush.talkio.utils

fun getOtherUserId(users: List<String>, userId: String) = users.first { it != userId }

