package com.ayush.talkio.presentation.screens.auth

import android.app.Activity

data class LoginUiState(
    val phone: String = "",
    val activity: Activity = Activity(),
    val phoneError: Boolean = true
)