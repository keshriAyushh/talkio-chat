package com.ayush.talkio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.presentation.ui.theme.Cyan


@Composable
fun DialogScreen(
    userId: MutableState<String>,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.White)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyText(
                    text = "Send chat request",
                    fontSize = 18,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    font = R.font.bold
                )
                Space(
                    height = 10.dp
                )
                MyText(
                    text = "Enter user id to send chat request",
                    fontSize = 14,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    font = R.font.regular
                )
                Space(
                    height = 10.dp
                )
                MyTextField(
                    text = userId,
                    placeholder = "Enter user id",
                    maxLines = 10
                ) { userId ->

                }
                Space(height = 10.dp)
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            onDismiss()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Red
                        )
                    ) {
                        MyText(text = "Cancel", color = Color.Red)
                    }
                    Space(width = 10.dp)
                    TextButton(
                        onClick = {
                            onConfirm(userId.value)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Cyan
                        )
                    ) {
                        MyText(text = "Send", color = Color.Blue)
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AlertDialogScreenPreview() {
    DialogScreen(rememberSaveable() {
        mutableStateOf("")
    }, onDismiss = { /*TODO*/ }, onConfirm = { /*TODO*/ })
}