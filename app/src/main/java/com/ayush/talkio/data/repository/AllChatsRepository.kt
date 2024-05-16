package com.ayush.talkio.data.repository

import com.ayush.talkio.data.model.Chat
import com.ayush.talkio.utils.Response
import kotlinx.coroutines.flow.Flow

interface AllChatsRepository {

    fun getAllChatRooms(): Flow<Response<List<Chat>>>
    fun sendChatRequest(to: String): Flow<Response<Boolean>>

    fun setFcmToken()

}