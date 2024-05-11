package com.ayush.talkio.presentation.viewmodels

import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.talkio.data.repository.AuthRepository
import com.ayush.talkio.presentation.events.AuthUiEvent
import com.ayush.talkio.presentation.rules.Validator
import com.ayush.talkio.presentation.screens.auth.LoginUiState
import com.ayush.talkio.utils.Response
import com.google.rpc.context.AttributeContext.Auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private var isValidated: Boolean = false

    var verificationCode: String = ""

    val loginUiState = mutableStateOf(LoginUiState())

    private val _sendOtpEvent = MutableStateFlow<Response<String>>(Response.None)
    val sendOtpEvent = _sendOtpEvent.asStateFlow()

    fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.OnPhoneChanged -> {
                loginUiState.value = loginUiState.value.copy(
                    phone = event.phone
                )
                validateResult()
            }
            is AuthUiEvent.OnSendOtpClicked -> {
                if(isValidated) {
                    sendOtp(loginUiState.value.phone, activity = loginUiState.value.activity)
                }
            }
        }
    }

    private fun sendOtp(phone: String, activity: Activity) {
        viewModelScope.launch {
            authRepository.sendOtp(phone = phone, activity = activity)
                .collect {
                    _sendOtpEvent.value = it
                }
        }
    }

    private fun validateResult() {
        loginUiState.value = loginUiState.value.copy(
            phoneError = Validator.validatePhone(loginUiState.value.phone).status
        )

        isValidated = Validator.validatePhone(loginUiState.value.phone).status
    }
}