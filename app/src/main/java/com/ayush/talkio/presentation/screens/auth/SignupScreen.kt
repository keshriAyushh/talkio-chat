package com.ayush.talkio.presentation.screens.auth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.data.model.User
import com.ayush.talkio.presentation.components.Loading
import com.ayush.talkio.presentation.components.MyTextField
import com.ayush.talkio.presentation.components.Space
import com.ayush.talkio.presentation.ui.theme.Cyan
import com.ayush.talkio.presentation.ui.theme.Surface
import com.ayush.talkio.presentation.viewmodels.AuthViewModel
import com.ayush.talkio.utils.LocalActivity
import com.ayush.talkio.utils.LocalAuthNavigator
import com.ayush.talkio.utils.LocalSnackbarState
import com.ayush.talkio.utils.Response
import com.ayush.talkio.utils.Route
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {

    val snackbarHost = LocalSnackbarState.current
    val scope = rememberCoroutineScope()
    val navigator = LocalAuthNavigator.current
    val activity = LocalActivity.current
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val name = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }

    viewModel.signUpEvent.collectAsState().value.let {
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

            Response.Loading -> {
                Loading()
            }

            Response.None -> {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHost)
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Surface)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {


                            MyText(
                                text = "Talkio",
                                fontSize = 25,
                                color = Color.Black,
                                font = R.font.bold
                            )

                            Space(height = 10.dp)

                            MyText(
                                text = "Enter your details to get started",
                                color = Color.Black,
                                fontSize = 20,
                                font = R.font.medium,
                                textAlign = TextAlign.Center
                            )

                            Space(height = 20.dp)

                            Image(
                                painter = painterResource(id = R.drawable.chat_image),
                                contentDescription = "otp_image"
                            )

                            Space(height = 20.dp)

                            MyTextField(
                                text = name,
                                trailingIcon = Icons.Rounded.PersonOutline,
                                color = Color.Black,
                                visualTransformation = VisualTransformation.None,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                placeholder = "Name",
                            ) { newName -> }

                            Space(height = 10.dp)

                            MyTextField(
                                text = email,
                                trailingIcon = Icons.Rounded.PersonOutline,
                                color = Color.Black,
                                visualTransformation = VisualTransformation.None,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                placeholder = "Email"
                            ) { newEmail -> }

                            Space(height = 10.dp)

                            MyTextField(
                                text = password,
                                trailingIcon = if (passwordVisible.value) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                                color = Color.Black,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                onTrailingIconClick = {
                                    passwordVisible.value = !passwordVisible.value
                                },
                                visualTransformation = if (!passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
                                placeholder = "Password"
                            ) { newPassword ->

                            }

                            Space(height = 30.dp)

                            Button(
                                onClick = {
                                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.value)
                                            .matches()
                                    ) {
                                        if (password.value.length >= 6) {
                                            if (name.value.isNotEmpty()) {
                                                viewModel.signUp(
                                                    User(
                                                        email = email.value,
                                                        password = password.value,
                                                        name = name.value,
                                                        isOnline = false,
                                                        lastSeen = -1L,
                                                        bio = "",
                                                        pfp = "",
                                                        fcmToken = "",
                                                        timestamp = 0L,
                                                        userId = ""
                                                    )
                                                )
                                            } else {
                                                Toast.makeText(
                                                    activity,
                                                    "Please enter your name",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            Toast.makeText(
                                                activity,
                                                "Password must be at least 6 characters",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            "Invalid Email",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Cyan,
                                    contentColor = Color.Black
                                )
                            ) {
                                MyText(
                                    text = "Create Account",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Transparent),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                MyText(
                                    text = "Already have an account? Sign in",
                                    fontSize = 14,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    font = R.font.medium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.clickable {
                                        navigator.navigate(Route.LoginScreen.route)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            is Response.Success -> {
                if (it.data) {
                    navigator.navigate(Route.CompleteProfileScreen.route)
                } else {
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}