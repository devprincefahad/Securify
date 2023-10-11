package dev.prince.securify.ui.generate

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.LightBlue
import dev.prince.securify.ui.theme.LightGray
import dev.prince.securify.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun GenerateScreen(
    viewModel: GenerateViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val interactionSource = MutableInteractionSource()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Password Generator"
                    )
                }
            )
        }
    ) { innerPadding ->

        LaunchedEffect(Unit) {
            viewModel.messages.collect {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .height(160.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, LightGray),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Text(
                        text = viewModel.password,
                        color = Color.Black,
                        fontSize = 26.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                viewModel.password = viewModel.generatePassword(
                                    viewModel.passwordLength,
                                    viewModel.lowerCase,
                                    viewModel.upperCase,
                                    viewModel.digits,
                                    viewModel.specialCharacters
                                )
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Options",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontFamily = poppinsFamily
            )

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Length",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontFamily = poppinsFamily
                )
                CustomSlider(
                    value = viewModel.passwordLength.toFloat(),
                    onValueChange = { viewModel.passwordLength = it.toInt() },
                    valueRange = 8f..15f,
                    interactionSource = interactionSource
                )
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lower case",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontFamily = poppinsFamily
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.lowerCase,
                    onCheckedChange = {
                        viewModel.lowerCase = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Blue,
                        uncheckedTrackColor = LightBlue,
                        uncheckedBorderColor = LightBlue,
                        uncheckedThumbColor = Color.White
                    )
                )
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upper case",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontFamily = poppinsFamily
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.upperCase,
                    onCheckedChange = { viewModel.upperCase = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Blue,
                        uncheckedTrackColor = LightBlue,
                        uncheckedBorderColor = LightBlue,
                        uncheckedThumbColor = Color.White
                    )
                )
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Digits",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontFamily = poppinsFamily
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.digits,
                    onCheckedChange = { viewModel.digits = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Blue,
                        uncheckedTrackColor = LightBlue,
                        uncheckedBorderColor = LightBlue,
                        uncheckedThumbColor = Color.White
                    )
                )
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Special characters",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontFamily = poppinsFamily
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.specialCharacters,
                    onCheckedChange = { viewModel.specialCharacters = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Blue,
                        uncheckedTrackColor = LightBlue,
                        uncheckedBorderColor = LightBlue,
                        uncheckedThumbColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(
                        start = 16.dp, end = 16.dp,
                        bottom = 40.dp
                    )
                    .fillMaxWidth()
                    .height(60.dp),
                onClick = {
                    clipboardManager.setText(
                        AnnotatedString((viewModel.password))
                    )
                    viewModel.showCopyMsg()
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Copy Password",
                    fontSize = 22.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }

    BackHandler {
        (context as ComponentActivity).finish()
    }
}

@Preview(showBackground = true)
@Composable
fun GenerateScreenPreview() {
    GenerateScreen()
}
