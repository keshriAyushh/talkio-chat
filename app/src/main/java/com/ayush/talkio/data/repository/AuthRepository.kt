package com.ayush.talkio.data.repository

import com.ayush.talkio.data.model.User
import com.ayush.talkio.utils.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signUp(user: User): Flow<Response<Boolean>>
    fun signIn(email: String, password: String): Flow<Response<Boolean>>
    fun logout()
}