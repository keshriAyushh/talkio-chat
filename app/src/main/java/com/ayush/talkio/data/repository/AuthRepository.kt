package com.ayush.talkio.data.repository

import android.app.Activity
import com.ayush.talkio.utils.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun sendOtp(phone: String, activity: Activity): Flow<Response<String>>
    fun verifyOtp(phone: String, otp: String, verificationCode: String): Flow<Response<Boolean>>
    fun logout()
}