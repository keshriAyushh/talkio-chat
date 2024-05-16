package com.ayush.talkio.presentation.screens.main.profile

import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.presentation.components.Loading
import com.ayush.talkio.presentation.components.MyTextField
import com.ayush.talkio.presentation.components.Space
import com.ayush.talkio.presentation.ui.theme.Cyan
import com.ayush.talkio.presentation.ui.theme.Surface
import com.ayush.talkio.presentation.viewmodels.ProfileViewModel
import com.ayush.talkio.utils.LocalAuthNavigator
import com.ayush.talkio.utils.Response
import com.ayush.talkio.utils.Route

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun CompleteProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val bio = rememberSaveable {
        mutableStateOf("")
    }
    val navigator = LocalAuthNavigator.current
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Gallery permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    viewModel.completeProfileEvent.collectAsState().value.let {
        when (it) {
            is Response.Error -> {
                Toast.makeText(LocalContext.current, it.error, Toast.LENGTH_SHORT).show()
            }

            Response.Loading -> Loading()
            Response.None -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Surface)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(Surface),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Surface)
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            IconButton(
                                onClick = {
                                    navigator.popBackStack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "back"
                                )
                            }
                            Space(width = 10.dp)
                            MyText(
                                text = "Talkio",
                                fontSize = 16,
                                color = Color.Black,
                                fontWeight = FontWeight.Normal,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            MyText(
                                text = "Skip",
                                fontSize = 16,
                                color = Color.Black,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                    }

                    Space(height = 30.dp)

                    if (imageUri != null) {

                        Card(
                            modifier = Modifier
                                .background(Color.Transparent)
                                .align(Alignment.CenterHorizontally),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = imageUri),
                                contentDescription = "pfp",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(100.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    } else {
                        Card(
                            modifier = Modifier
                                .background(Color.Transparent)
                                .align(Alignment.CenterHorizontally),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "pfp",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(100.dp)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                galleryLauncher.launch("image/*")
                            } else {
                                // Request the gallery permission using the launcher
                                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        },
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Cyan,
                            contentColor = Color.Black
                        )
                    ) {
                        MyText(text = "Upload Profile Picture")
                    }

                    Space(height = 10.dp)

                    MyTextField(
                        text = bio,
                        placeholder = "Write something about yourself",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions.Default,
                        modifier = Modifier.height(200.dp),
                        maxLines = 10
                    ) { newBio -> }

                    Button(
                        onClick = {
                            viewModel.completeProfile(bio = bio.value, profilePic = imageUri)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Cyan,
                            contentColor = Color.Black
                        )
                    ) {
                        MyText(text = "Save")
                    }
                }
            }

            is Response.Success -> {
                if (it.data) {
                    navigator.navigate(Route.MainScreen.route) {
                        popUpTo(Route.MainScreen.route) {
                            inclusive = true
                        }
                    }
                } else {
                    Toast.makeText(LocalContext.current, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}