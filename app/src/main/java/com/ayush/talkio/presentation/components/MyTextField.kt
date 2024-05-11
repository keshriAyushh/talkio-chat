package com.ayush.talkio.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import com.ayush.convoz.presentation.components.MyText
import com.ayush.talkio.R
import com.ayush.talkio.presentation.ui.theme.Error


@Composable
fun MyTextField(
    text: MutableState<String>,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    trailingIcon: ImageVector? = null,
    placeholder: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: Boolean = true,
    prefix: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { newText ->
            text.value = newText
            onValueChange(newText)
        },
        placeholder = @Composable {
            placeholder?.let { value ->
                MyText(text = value, fontSize = 14, color = Color.Gray)
            }
        },
        trailingIcon = @Composable {
            trailingIcon?.let {
                IconButton(
                    onClick = {
                        if (onTrailingIconClick != null) {
                            onTrailingIconClick()
                        }
                    }
                ) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = "trailing_icon",
                    )
                }
            }
        },
        prefix = @Composable {
            prefix?.let {
                prefix()
            }
        },
        supportingText = {
            if (!error && placeholder == "Password") {
                MyText(
                    text = "* Password must be at least 6 characters long",
                    fontSize = 12,
                    font = R.font.bold,
                    color = Color.Red
                )
            }
        },

        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = color,
            disabledTextColor = Color.Gray,
            unfocusedTextColor = Color.DarkGray,
            errorTextColor = Color.Black,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTrailingIconColor = Color.Black,
            unfocusedTrailingIconColor = Color.Gray,
            errorTrailingIconColor = Error,
            errorContainerColor = Color.Transparent,
            errorBorderColor = Error,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            errorSupportingTextColor = Error
        ),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        isError = !error,
        maxLines = 1
    )
}