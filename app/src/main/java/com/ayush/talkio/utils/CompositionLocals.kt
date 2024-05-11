package com.ayush.talkio.utils

import android.app.Activity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalAuthNavigator = compositionLocalOf<NavHostController> {
    error("error")
}

val LocalAppNavigator = compositionLocalOf<NavHostController> {
    error("error")
}

val LocalSnackbarState = compositionLocalOf<SnackbarHostState> {
    error("error")
}

val LocalActivity = compositionLocalOf<Activity> {
    error("error")
}
