package com.ayush.talkio.presentation.events

import android.app.Activity

sealed class AuthUiEvent {
    data class OnPhoneChanged(val phone: String): AuthUiEvent()
    data class OnSendOtpClicked(val phone: String, val activity: Activity): AuthUiEvent()
}