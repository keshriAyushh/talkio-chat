package com.ayush.talkio.utils

object Constants {

    const val COMPRESSION_QUALITY: Int = 50
    const val USERS_REF = "users"
    const val CHATROOM_COLLECTION = "chatrooms"
    const val ERR = "An unknown error has occurred."

    val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.READ_MEDIA_VIDEO,
        android.Manifest.permission.READ_MEDIA_AUDIO,
        android.Manifest.permission.READ_MEDIA_IMAGES
    )
}