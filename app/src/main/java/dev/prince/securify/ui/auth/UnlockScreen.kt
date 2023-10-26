package dev.prince.securify.ui.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.prince.securify.R
import dev.prince.securify.ui.composables.BottomSheetSurface
import dev.prince.securify.ui.destinations.IntroScreenDestination
import dev.prince.securify.ui.destinations.PasswordsScreenDestination
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.LocalSnackbar

@Composable
@RootNavGraph(start = true)
@Destination
fun UnlockScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val snackbar = LocalSnackbar.current

    LaunchedEffect(Unit) {
        viewModel.messages.collect {
            snackbar(it)
        }
    }

    val context = LocalContext.current as FragmentActivity

    //For Biometrics
    val executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = BiometricPrompt(
        context,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
//                viewModel.showSnackBarMsg("$errorCode :: $errString")
            }

            @RequiresApi(Build.VERSION_CODES.R)
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                navigator.navigate(PasswordsScreenDestination)
                viewModel.showSnackBarMsg("Authentication was successful")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                viewModel.showSnackBarMsg("Authentication failed for an unknown reason")
            }
        }
    )

    LaunchedEffect(Unit) {
        if (viewModel.isUserLoggedIn && viewModel.isBiometricSupported() && viewModel.getState) {
            biometricPrompt.authenticate(viewModel.promptInfo)
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
        UnlockScreenContent()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UnlockScreenContent(
    viewModel: AuthViewModel = hiltViewModel()
) {

    val keyboard = LocalSoftwareKeyboardController.current

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
            BottomSheetSurface {
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
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = poppinsFamily
                        ),
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
                            if (it.length <= 8) viewModel.unlockKey = it
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Gray
                        ),
                        singleLine = true,
                        supportingText = {
                            AnimatedVisibility(viewModel.unlockKey.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Medium,
                                    text = "Limit: ${viewModel.unlockKey.length}/8",
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboard?.hide()
                                viewModel.validateAndOpen()
                            }
                        ),
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
                            viewModel.validateAndOpen()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
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

        Image(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .offset(y = (80).dp),
            painter = painterResource(R.drawable.key),
            contentDescription = null
        )
    }
}