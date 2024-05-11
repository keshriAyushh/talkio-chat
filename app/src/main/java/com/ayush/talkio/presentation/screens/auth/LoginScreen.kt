package com.ayush.talkio.presentation.screens.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.presentation.components.Loading
import com.ayush.talkio.presentation.components.MyTextField
import com.ayush.talkio.presentation.components.Space
import com.ayush.talkio.presentation.events.AuthUiEvent
import com.ayush.talkio.presentation.ui.theme.Surface
import com.ayush.talkio.presentation.viewmodels.AuthViewModel
import com.ayush.talkio.utils.LocalActivity
import com.ayush.talkio.utils.LocalAuthNavigator
import com.ayush.talkio.utils.LocalSnackbarState
import com.ayush.talkio.utils.Response
import com.ayush.talkio.utils.Route
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {

    val snackbarHost = LocalSnackbarState.current
    val scope = rememberCoroutineScope()
    val navigator = LocalAuthNavigator.current
    val activity = LocalActivity.current
    val phone = rememberSaveable {
        mutableStateOf("")
    }

    viewModel.sendOtpEvent.collectAsState().value.let {
        when (it) {
            is Response.Error -> {
                scope.launch {
                    snackbarHost
                        .showSnackbar(
                            message = it.error,
                            duration = SnackbarDuration.Short
                        )
                }
            }

            Response.Loading -> { Loading() }
            Response.None -> {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHost)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Surface)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MyText(
                            text = "Talkio",
                            fontSize = 25,
                            color = Color.Black,
                            font = R.font.bold
                        )

                        Space(height = 10.dp)

                        MyText(
                            text = "Enter your phone number to get started",
                            color = Color.Black,
                            fontSize = 20,
                            font = R.font.medium,
                            textAlign = TextAlign.Center
                        )

                        Space(height = 20.dp)

                        Image(
                            painter = painterResource(id = R.drawable.otp),
                            contentDescription = "otp_image"
                        )

                        Space(height = 20.dp)

                        MyTextField(
                            text = phone,
                            placeholder = "Phone Number",
                            trailingIcon = Icons.Filled.Phone,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done
                            ),
                            prefix = {
                                MyText(
                                    text = "+91",
                                    color = Color.Black,
                                    font = R.font.bold
                                )
                            },
                            visualTransformation = VisualTransformation.None,
                            error = viewModel.loginUiState.value.phoneError
                        ) { newPhone ->
                            viewModel.onEvent(AuthUiEvent.OnPhoneChanged(newPhone))
                        }

                        Space(height = 10.dp)

                        Button(
                            onClick = {
                                viewModel.onEvent(AuthUiEvent.OnSendOtpClicked(phone.value, activity = activity))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            MyText(
                                text = "Send OTP",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            is Response.Success -> {
                if (it.data.isNotBlank() || it.data.isNotEmpty()) {
                    viewModel.verificationCode = it.data
                    Log.d("code", it.data)
                    navigator.navigate(Route.VerifyCodeScreen.route)
                } else {
                    scope.launch {
                        snackbarHost
                            .showSnackbar(
                                message = "Something went wrong",
                                duration = SnackbarDuration.Short
                            )
                    }
                }
            }
        }
    }
}