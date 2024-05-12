package com.ayush.talkio.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayush.talkio.presentation.screens.main.MainScreen
import com.ayush.talkio.presentation.screens.main.profile.CompleteProfileScreen
import com.ayush.talkio.utils.LocalAuthNavigator
import com.ayush.talkio.utils.Route

@Composable
fun Navigation() {

    val authNavController = rememberNavController()
    CompositionLocalProvider(LocalAuthNavigator provides authNavController) {
        NavHost(
            navController = authNavController,
            startDestination = Route.AUTH.route
        ) {
            authNavGraph()
            mainNavGraph()
        }
    }
}