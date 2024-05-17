package com.ayush.talkio.presentation.screens.main

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ayush.talkio.navigation.BottomNavGraph
import com.ayush.talkio.presentation.ui.theme.Cyan
import com.ayush.talkio.presentation.ui.theme.Surface
import com.ayush.talkio.utils.BtmRoute
import com.ayush.talkio.utils.LocalAuthNavigator
import com.ayush.talkio.utils.LocalSnackbarState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val appNavController = rememberNavController()
    val authNavController = LocalAuthNavigator.current
    val snackbarHostState = LocalSnackbarState.current


    val navBackStackEntry by appNavController.currentBackStackEntryAsState()
    var bottomBarState by rememberSaveable {
        mutableStateOf(false)
    }

    when (navBackStackEntry?.destination?.route) {
        BtmRoute.AllChats.route -> {
            bottomBarState = true
        }

        BtmRoute.Stories.route -> {
            bottomBarState = true
        }

        BtmRoute.Profile.route -> {
            bottomBarState = true
        }

        BtmRoute.Requests.route -> {
            bottomBarState = true
        }

        BtmRoute.Chat.route -> {
            bottomBarState = false
        }
    }

    Scaffold(
        bottomBar = {
            if (bottomBarState) {
                BottomBar(appNavController)
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) {
        BottomNavGraph(
            appNavHostController = appNavController,
            authNavController = authNavController
        )
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val screens = listOf(
        BtmRoute.AllChats,
        BtmRoute.Stories,
        BtmRoute.Requests,
        BtmRoute.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = Surface,
        contentColor = Color.Black,
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navHostController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BtmRoute,
    currentDestination: NavDestination?,
    navHostController: NavController
) {
    val iconAnim = animateDpAsState(targetValue = 40.dp, label = "iconAnim")

    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true
    NavigationBarItem(
        selected = isSelected,
        onClick = {
            navHostController.navigate(screen.route) {
                popUpTo(navHostController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "bottom_bar_icon",
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            unselectedIconColor = Color.Gray,
            indicatorColor = Cyan
        ),
        alwaysShowLabel = true,
        modifier = Modifier.size(iconAnim.value)
    )
}
