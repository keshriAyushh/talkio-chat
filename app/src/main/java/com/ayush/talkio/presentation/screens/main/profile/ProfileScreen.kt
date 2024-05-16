package com.ayush.talkio.presentation.screens.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ayush.talkio.presentation.ui.theme.Surface
import com.ayush.talkio.presentation.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    authNavController: NavController = rememberNavController(),
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            viewModel.logout()
            onLogout()

        }) {
            Text(text = "Log out")
        }
    }
}