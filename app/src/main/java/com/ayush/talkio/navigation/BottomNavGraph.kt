package com.ayush.talkio.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ayush.talkio.presentation.screens.main.RequestsScreen
import com.ayush.talkio.presentation.screens.main.chats.AllChatsScreen
import com.ayush.talkio.presentation.screens.main.chats.ChatScreen
import com.ayush.talkio.presentation.screens.main.profile.ProfileScreen
import com.ayush.talkio.presentation.screens.main.story.StoryScreen
import com.ayush.talkio.utils.BtmRoute
import com.ayush.talkio.utils.LocalAppNavigator
import com.ayush.talkio.utils.Route

@Composable
fun BottomNavGraph(
    authNavController: NavHostController,
    appNavHostController: NavHostController
) {
    CompositionLocalProvider(LocalAppNavigator provides appNavHostController) {
        NavHost(
            navController = appNavHostController,
            startDestination = BtmRoute.AllChats.route,
            route = Route.APP.route
        ) {
            composable(route = BtmRoute.AllChats.route) {
                AllChatsScreen()
            }
            composable(route = BtmRoute.Profile.route) {
                ProfileScreen {
                    authNavController.navigate(Route.LoginScreen.route) {
                        popUpTo(Route.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
            composable(route = BtmRoute.Stories.route) {
                StoryScreen()
            }
            composable(route = BtmRoute.Requests.route) {
                RequestsScreen()
            }
            composable("${BtmRoute.Chat.route}/{userId}") { backStackEntry ->
                ChatScreen(receiverId = backStackEntry.arguments?.getString("userId")!!)
            }
        }
    }
}