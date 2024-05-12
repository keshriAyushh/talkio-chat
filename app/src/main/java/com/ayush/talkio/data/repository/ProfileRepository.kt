package com.ayush.talkio.data.repository

import android.net.Uri
import com.ayush.talkio.utils.Response
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun completeProfile(bio: String?, profilePic: Uri?): Flow<Response<Boolean>>
}