package com.ayush.talkio.data.repository

import com.ayush.talkio.data.model.ChatRequest
import com.ayush.talkio.utils.Response
import kotlinx.coroutines.flow.Flow

interface RequestsRepository {

    fun getAllRequests(): Flow<Response<List<ChatRequest>>>
    fun rejectRequest(request: ChatRequest)
    fun acceptRequest(request: ChatRequest): Flow<Response<Boolean>>
}