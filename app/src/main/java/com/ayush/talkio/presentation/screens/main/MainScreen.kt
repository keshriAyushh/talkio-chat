package com.ayush.talkio.presentation.screens.main

import androidx.compose.runtime.Composable
import com.ayush.talkio.utils.LocalAppNavigator
import com.ayush.talkio.utils.LocalAuthNavigator

@Composable
fun MainScreen() {
    val authNavController = LocalAuthNavigator.current
    val appNavController = LocalAppNavigator.current
}