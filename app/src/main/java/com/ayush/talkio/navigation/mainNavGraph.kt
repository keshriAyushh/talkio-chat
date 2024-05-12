package com.ayush.talkio.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ayush.talkio.presentation.screens.main.MainScreen
import com.ayush.talkio.presentation.screens.main.profile.CompleteProfileScreen
import com.ayush.talkio.utils.Route

fun NavGraphBuilder.mainNavGraph() {
    navigation(
        startDestination = Route.MainScreen.route,
        route = Route.MAIN.route
    ) {
        composable(route = Route.MainScreen.route) {
            MainScreen()
        }
    }
}