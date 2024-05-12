package com.ayush.talkio.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ayush.talkio.presentation.screens.auth.LoginScreen
import com.ayush.talkio.presentation.screens.auth.SignupScreen
import com.ayush.talkio.presentation.screens.main.profile.CompleteProfileScreen
import com.ayush.talkio.utils.Route

fun NavGraphBuilder.authNavGraph() {
    navigation(
        startDestination = Route.SignupScreen.route,
        route = Route.AUTH.route
    ) {
        composable(route = Route.LoginScreen.route) {
            LoginScreen()
        }

        composable(route = Route.SignupScreen.route) {
            SignupScreen()
        }

        composable(route = Route.CompleteProfileScreen.route) {
            CompleteProfileScreen()
        }
    }
}