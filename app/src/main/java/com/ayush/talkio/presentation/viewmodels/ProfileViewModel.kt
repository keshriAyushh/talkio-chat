package com.ayush.talkio.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.talkio.data.repository.AuthRepository
import com.ayush.talkio.data.repository.ProfileRepository
import com.ayush.talkio.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _completeProfileEvent = MutableStateFlow<Response<Boolean>>(Response.None)
    val completeProfileEvent = _completeProfileEvent.asStateFlow()


    fun completeProfile(profilePic: Uri?, bio: String?) {
        viewModelScope.launch {
            profileRepository.completeProfile(bio = bio, profilePic = profilePic)
                .collect {
                    _completeProfileEvent.value = it
                }
        }
    }


    fun logout() {
        authRepository.logout()
    }

}