package dev.prince.securify.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.prince.securify.R
import dev.prince.securify.ui.destinations.IntroScreenDestination
import dev.prince.securify.ui.destinations.PasswordsScreenDestination
import dev.prince.securify.ui.theme.LightBlue
import dev.prince.securify.ui.theme.poppinsFamily

@Composable
@Destination(start = true)
fun UnlockScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->

        LaunchedEffect(Unit) {
            viewModel.messages.collect {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
            }
        }

        LaunchedEffect(Unit) {
            viewModel.navigateToHome.collect {
                navigator.navigate(PasswordsScreenDestination) {
                    popUpTo(PasswordsScreenDestination) {
                        inclusive = true
                    }
                }
            }
        }

        if (!viewModel.isUserLoggedIn) {
            navigator.navigate(IntroScreenDestination)
        } else {
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .background(color = Color.Black)
            ) {

                Spacer(modifier = Modifier.height(120.dp))
                Image(
                    modifier = Modifier
                        .height(160.dp)
                        .fillMaxWidth(),
                    painter = painterResource(R.drawable.key),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(160.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .padding(all = 8.dp)
                                .fillMaxWidth(),
                            text = "Please provide your \nMaster Key",
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Enter Master key") },
                            value = viewModel.unlockKey,
                            visualTransformation = if (viewModel.unlockKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            onValueChange = {
                                viewModel.unlockKey = it
                                viewModel.validateUnlockKey(viewModel.unlockKey)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor = Color.Gray,
                                cursorColor = Color.Gray,
                                errorBorderColor = Color.Red,
                                errorTextColor = Color.Red,
                                errorSupportingTextColor = Color.Red,
                                errorLabelColor = Color.Red
                            ),
                            singleLine = true,
                            isError = viewModel.isErrorForUnlock,
                            supportingText = {
                                if (viewModel.isErrorForUnlock) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Red,
                                        text = "Limit: ${viewModel.unlockKey.length}/${viewModel.maxLength}",
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        viewModel.unlockKeyVisible = !viewModel.unlockKeyVisible
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (viewModel.unlockKeyVisible)
                                            Icons.Filled.Visibility
                                        else Icons.Filled.VisibilityOff,
                                        if (viewModel.unlockKeyVisible) "Hide password" else "Show password",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp,
                                    start = 16.dp, end = 16.dp
                                )
                                .fillMaxWidth()
                                .height(50.dp),
                            onClick = {
                                viewModel.proceedValidation()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightBlue,
                                contentColor = Color.White
                            )
                        ) {
                            if (viewModel.isLoadingForUnlock) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Proceed",
                                    fontSize = 22.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}