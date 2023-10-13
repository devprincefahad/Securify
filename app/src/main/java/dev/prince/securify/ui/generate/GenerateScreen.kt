package dev.prince.securify.ui.generate

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        /*topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Password Generator",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFamily
                    )
                },
                colors = TopAppBarDefaults
                    .smallTopAppBarColors(
                        containerColor = Color.Black
                    )
            )
        }*/
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
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color.Black),
            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 24.dp,
                        bottom = 16.dp
                    )
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
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFamily
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
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
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Options",
                        fontSize = 24.sp,
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
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
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
                            fontSize = 18.sp,
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
                            fontSize = 18.sp,
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
                            fontSize = 18.sp,
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
                            fontSize = 18.sp,
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

                    Row(
                        modifier = Modifier
                            .padding(
                                start = 22.dp,
                                end = 22.dp,
                                bottom = 32.dp
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Card(
                            modifier = Modifier
                                .size(52.dp)
                                .background(color = Color.Black, shape = CircleShape),
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            ),
                            onClick = {
                                if (!(viewModel.lowerCase || viewModel.upperCase ||
                                            viewModel.digits || viewModel.specialCharacters)
                                ) {
                                    viewModel.showUncheckedToggleMsg()
                                } else {
                                    viewModel.password = viewModel.generatePassword(
                                        viewModel.passwordLength,
                                        viewModel.lowerCase,
                                        viewModel.upperCase,
                                        viewModel.digits,
                                        viewModel.specialCharacters
                                    )
                                }
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .size(40.dp),
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(58.dp)
                                .padding(
                                    start = 16.dp
                                ),
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
                                fontSize = 20.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
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
