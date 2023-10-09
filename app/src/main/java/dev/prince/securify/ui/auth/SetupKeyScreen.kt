package dev.prince.securify.ui.auth

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import dev.prince.securify.ui.destinations.PasswordsScreenDestination
import dev.prince.securify.ui.theme.LightBlue
import dev.prince.securify.ui.theme.poppinsFamily
import kotlinx.coroutines.launch

@Destination
@Composable
fun SetupKeyScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
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

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 32.dp),
                text = stringResource(R.string.setup_key_tagline_1),
                textAlign = TextAlign.Left,
                fontSize = 52.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(80.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .wrapContentHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = 24.dp, topEnd = 24.dp
                        )
                    ),
                shape = RoundedCornerShape(
                    bottomStart = 0.dp, bottomEnd = 0.dp
                ),
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
                        text = stringResource(R.string.setup_master_key),
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.setup_key_tagline_2),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Enter Master key") },
                        value = viewModel.key,
                        onValueChange = {
                            viewModel.key = it
                            viewModel.validateKey(viewModel.key)
                        },
                        isError = viewModel.isErrorForKey,
                        supportingText = {
                            if (viewModel.isErrorForKey) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Limit: ${viewModel.key.length}/${viewModel.maxLength}",
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Red,
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
                            cursorColor = Color.Gray,
                            errorBorderColor = Color.Red,
                            errorTextColor = Color.Red,
                            errorSupportingTextColor = Color.Red,
                            errorLabelColor = Color.Red
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.keyVisible = !viewModel.keyVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (viewModel.keyVisible)
                                        Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff, contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Confirm Master key") },
                        value = viewModel.confirmKey,
                        visualTransformation = if (viewModel.confirmKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        onValueChange = {
                            viewModel.confirmKey = it
                            viewModel.validateConfirmKey(viewModel.confirmKey)
                        },
                        isError = viewModel.isErrorForConfirmKey,
                        supportingText = {
                            if (viewModel.isErrorForConfirmKey) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Limit: ${viewModel.confirmKey.length}/${viewModel.maxLength}",
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Red,
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
                            cursorColor = Color.Gray,
                            errorBorderColor = Color.Red,
                            errorTextColor = Color.Red,
                            errorSupportingTextColor = Color.Red,
                            errorLabelColor = Color.Red
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.confirmKeyVisible = !viewModel.confirmKeyVisible
                            }) {
                                Icon(
                                    imageVector = if (viewModel.confirmKeyVisible)
                                        Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff,
                                    if (viewModel.confirmKeyVisible) "Hide password" else "Show password",
                                    tint = Color.Gray
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            viewModel.saveMasterKeyValidation()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightBlue,
                            contentColor = Color.White
                        )
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Save Master Key",
                                fontSize = 22.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            BackHandler {
                (context as ComponentActivity).finish()
            }
        }
    }
}
