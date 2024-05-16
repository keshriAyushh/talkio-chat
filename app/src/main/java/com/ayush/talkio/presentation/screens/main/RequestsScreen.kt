package com.ayush.talkio.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.presentation.components.Loading
import com.ayush.talkio.presentation.components.RequestItem
import com.ayush.talkio.presentation.components.Space
import com.ayush.talkio.presentation.ui.theme.Surface
import com.ayush.talkio.presentation.viewmodels.RequestsViewModel
import com.ayush.talkio.utils.LocalSnackbarState
import com.ayush.talkio.utils.Response
import kotlinx.coroutines.launch

@Composable
fun RequestsScreen(
    viewModel: RequestsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbar = LocalSnackbarState.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllRequests()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = Surface,
                contentColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 10.dp
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    MyText(
                        text = "Requests",
                        color = Color.Black,
                        fontSize = 22,
                        fontWeight = FontWeight.Bold,
                        font = R.font.bold
                    )
                }
            }
        }
        Space(height = 10.dp)
        viewModel.allRequest.collectAsState().value.let {
            when (it) {
                is Response.Error -> {
                    scope.launch {
                        snackbar.showSnackbar(
                            message = it.error,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                Response.Loading -> {
                    Loading()
                }

                Response.None -> {

                }

                is Response.Success -> {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        items(it.data) { request ->
                            RequestItem(
                                request = request,
                                onReject = { req ->
                                    viewModel.rejectRequest(req)
                                },
                                onAccept = { req ->
                                    viewModel.acceptRequest(req)
                                }
                            )
                            Space(height = 4.dp)
                        }
                    }
                }
            }
        }
    }
}
