package com.ayush.talkio.utils

fun getChatroomId(
    userId1: String,
    userId2: String
): String {
    return if (userId1.hashCode() < userId2.hashCode()) {
        "${userId1}_$userId2"
    } else {
        "${userId2}_$userId1"
    }
}