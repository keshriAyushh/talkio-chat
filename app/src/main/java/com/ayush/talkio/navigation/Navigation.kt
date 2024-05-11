package com.ayush.talkio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayush.talkio.presentation.screens.main.MainScreen
import com.ayush.talkio.utils.Route

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.AUTH.route
    ) {
        authNavGraph()
        composable(route = Route.MAIN.route) {
            MainScreen()
        }
    }


}