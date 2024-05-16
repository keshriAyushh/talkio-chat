package com.ayush.talkio.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.talkio.data.model.User
import com.ayush.talkio.data.repository.AuthRepository
import com.ayush.talkio.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signUpEvent = MutableStateFlow<Response<Boolean>>(Response.None)
    val signUpEvent = _signUpEvent.asStateFlow()

    private val _signInEvent = MutableStateFlow<Response<Boolean>>(Response.None)
    val signInEvent = _signInEvent.asStateFlow()


    fun signUp(user: User) {
        viewModelScope.launch {
            authRepository.signUp(user = user)
                .collect {
                    _signUpEvent.value = it
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signIn(email, password)
                .collect {
                    _signInEvent.value = it
                }
        }
    }

    fun isLoggedIn() = authRepository.isLoggedIn()


    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}