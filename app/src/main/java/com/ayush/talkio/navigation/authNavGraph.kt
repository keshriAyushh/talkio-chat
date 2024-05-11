package com.ayush.talkio.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ayush.talkio.presentation.screens.auth.LoginScreen
import com.ayush.talkio.presentation.screens.auth.VerifyCodeScreen
import com.ayush.talkio.utils.Route

fun NavGraphBuilder.authNavGraph() {
    navigation(
        startDestination = Route.LoginScreen.route,
        route = Route.AUTH.route
    ) {
        composable(route = Route.LoginScreen.route) {
            LoginScreen()
        }

        composable(route = Route.VerifyCodeScreen.route) {
            VerifyCodeScreen()
        }
    }
}