package dev.prince.securify.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.prince.securify.R
import dev.prince.securify.ui.auth.AuthViewModel
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.Gray
import dev.prince.securify.ui.theme.poppinsFamily

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateMasterKeySheetContent(
    viewModel: AuthViewModel = hiltViewModel()
) {
    /*
       Add this code if sheet with scroll doesn't work
       or use box

   Column {
       Spacer(modifier = Modifier.weight(1f))
       SheetSurface {
           Column(
               modifier = Modifier
                   .padding(
                       top = 8.dp, bottom = 8.dp,
                       start = 16.dp, end = 16.dp
                   ).verticalScroll(rememberScrollState())
                   .fillMaxSize(),
               verticalArrangement = Arrangement.Bottom
           ) {
  */

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BgBlack)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {

            SheetSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center
                ) {

                    val keyboard = LocalSoftwareKeyboardController.current
                    val focusManager = LocalFocusManager.current

                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 6.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.setup_master_key),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 6.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.setup_key_tagline_2),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(top = 6.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = "Enter Master key",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    color = Gray
                                )
                            )
                        },
                        value = viewModel.key,
                        onValueChange = {
                            if (it.length <= 8) viewModel.key = it
                        },
                        supportingText = {
                            AnimatedVisibility(viewModel.key.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Limit: ${viewModel.key.length}/8",
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        },
                        visualTransformation = if (viewModel.keyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.keyVisible = !viewModel.keyVisible
                                }
                            ) {
                                Icon(
                                    painter = if (viewModel.keyVisible)
                                        painterResource(R.drawable.icon_visibility)
                                    else painterResource(R.drawable.icon_visibility_off),
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = "Confirm Master key",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    color = Gray
                                )
                            )
                        },
                        value = viewModel.confirmKey,
                        visualTransformation = if (viewModel.confirmKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        onValueChange = {
                            if (it.length <= 8) viewModel.confirmKey = it
                        },
                        supportingText = {
                            AnimatedVisibility(viewModel.confirmKey.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Limit: ${viewModel.confirmKey.length}/8",
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboard?.hide()
                                viewModel.validateAndSaveMasterKey()
                            }
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.confirmKeyVisible = !viewModel.confirmKeyVisible
                            }) {
                                Icon(
                                    painter = if (viewModel.confirmKeyVisible)
                                        painterResource(R.drawable.icon_visibility)
                                    else painterResource(R.drawable.icon_visibility_off), contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                    )

                    Button(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.validateAndSaveMasterKey()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = Color.White
                        )
                    ) {
                        AnimatedContent(viewModel.isLoading, label = "") {
                            if (it) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Save Master Key",
                                    style = TextStyle(
                                        fontSize = 22.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}