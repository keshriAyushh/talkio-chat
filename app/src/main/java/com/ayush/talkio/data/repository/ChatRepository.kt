package com.ayush.talkio.data.repository

import com.ayush.talkio.data.model.Message
import com.ayush.talkio.data.model.User
import com.ayush.talkio.utils.Response
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(receiverId: String): Flow<Response<List<Message>>>

    fun sendMessage(message: Message)
    fun getUser(userId: String): Flow<Response<User>>
}