package dev.prince.securify.ui.add

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.R
import dev.prince.securify.crypto.CryptoManager
import dev.prince.securify.ui.destinations.PasswordsScreenDestination
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AddPasswordScreen(
    navigator: DestinationsNavigator,
    viewModel: AddPasswordViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Add a new Password",
//                        textAlign = TextAlign.Center,
//                        fontSize = 24.sp,
//                        fontFamily = poppinsFamily,
//                        fontWeight = FontWeight.Medium
//                    )
//                },
//                navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            navigator.navigate(PasswordsScreenDestination)
//                        }) {
//                        Icon(
//                            imageVector = Icons.Filled.ArrowBackIos,
//                            contentDescription = null
//                        )
//                    }
//                }
//            )
//        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        val scrollState = rememberScrollState()
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
                .background(color = Color.Black)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(
                    modifier = Modifier.padding(
                        top = 14.dp, bottom = 14.dp,
                        start = 16.dp, end = 12.dp
                    ),
                    onClick = {
                        navigator.navigate(PasswordsScreenDestination)
                    }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIos,
                        tint = Color.White,
                        contentDescription = null
                    )
                }

                Text(
                    modifier = Modifier.padding(
                        top = 18.dp, bottom = 12.dp,
                        start = 16.dp, end = 16.dp
                    ),
                    text = "Add New Password",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFamily
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ), elevation = CardDefaults.cardElevation(
                    defaultElevation = 80.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .verticalScroll(state = scrollState)
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {

                    val optionsWithImages = listOf(
                        "Instagram" to painterResource(R.drawable.icon_instagram),
                        "Facebook" to painterResource(R.drawable.icon_facebook),
                        "LinkedIn" to painterResource(R.drawable.icon_linkedin),
                        "Snapchat" to painterResource(R.drawable.icons_snapchat),
                        "YouTube" to painterResource(R.drawable.icon_youtube),
                        "Netflix" to painterResource(R.drawable.icon_netflix),
                        "Discord" to painterResource(R.drawable.icon_discord),
                        "Twitter" to painterResource(R.drawable.icon_twitterx),
                        "Amazon Prime" to painterResource(R.drawable.icon_amazon_prime_video),
                        "Spotify" to painterResource(R.drawable.icon_spotify),
                        "Gmail" to painterResource(R.drawable.icon_gmail),
                        "Reddit" to painterResource(R.drawable.icon_reddit),
                        "Quora" to painterResource(R.drawable.icon_quora),
                        "Pinterest" to painterResource(R.drawable.icon_pinterest),
                        "Other" to painterResource(R.drawable.icon_others)
                    )

                    if (viewModel.selectedOption != null) {
                        Image(
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 16.dp)
                                .height(160.dp)
                                .fillMaxWidth(),
                            painter = viewModel.selectedOption!!.second,
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Account Name",
                        textAlign = TextAlign.Left,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable(onClick = { viewModel.expanded = true }),
                        expanded = viewModel.expanded,
                        onExpandedChange = { viewModel.expanded = !viewModel.expanded }
                    ) {

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    viewModel.textFieldSize = coordinates.size.toSize()
                                }
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor = Color.Gray,
                                cursorColor = Color.Gray
                            ),
                            readOnly = true,
                            value = viewModel.selectedOption?.first ?: "Choose an account",
                            onValueChange = { },
                            shape = RoundedCornerShape(8.dp),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (viewModel.expanded)
                                        Icons.Filled.ArrowDropUp
                                    else
                                        Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            },
                        )
                        ExposedDropdownMenu(
                            modifier = Modifier
                                .fillMaxWidth()
                                .width(with(LocalDensity.current) { viewModel.textFieldSize.width.toDp() })
                                .height(280.dp)
                                .background(Color.White),
                            expanded = viewModel.expanded,
                            onDismissRequest = {
                                viewModel.expanded = false
                            }
                        ) {
                            optionsWithImages.forEach { (selectionOption, painter) ->
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.selectedOption = selectionOption to painter
                                        viewModel.expanded = false
                                    },
                                    text = {
                                        Text(text = selectionOption)
                                    }
                                )
                            }
                        }
                    }

                    if (viewModel.selectedOption != null && viewModel.selectedOption?.first == "Other") {
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            value = viewModel.otherAccName,
                            placeholder = {
                                Text("Other Account Name")
                            },
                            shape = RoundedCornerShape(8.dp),
                            onValueChange = {
                                if (it.length <= 20) {
                                    viewModel.otherAccName = it
                                }
                            },
                            supportingText = {
                                Text(text = "${viewModel.otherAccName.length}/20")
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
                            leadingIcon = {
                                Icon(
                                    modifier = Modifier.size(22.dp),
                                    painter = painterResource(
                                        id = R.drawable.icon_other
                                    ),
                                    contentDescription = null
                                )
                            },
                            prefix = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextFieldSeparator()
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            )
                        )
                    }

                    Text(
                        text = "Username",
                        textAlign = TextAlign.Left,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        value = viewModel.username,
                        placeholder = {
                            Text("John Doe")
                        },
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            if (it.length <= 35) {
                                viewModel.username = it
                            }
                        },
                        supportingText = {
                            Text(text = "${viewModel.username.length}/35")
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
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(22.dp),
                                painter = painterResource(
                                    id = R.drawable.icon_user
                                ),
                                contentDescription = null
                            )
                        },
                        prefix = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextFieldSeparator()
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        )
                    )

                    Text(
                        text = "Email ID",
                        textAlign = TextAlign.Left,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        value = viewModel.email,
                        placeholder = {
                            Text("john@example.com")
                        },
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            if (it.length <= 35) {
                                viewModel.email = it
                            }
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
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(22.dp),
                                painter = painterResource(
                                    id = R.drawable.icon_email
                                ),
                                contentDescription = null
                            )
                        },
                        prefix = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextFieldSeparator()
                            }
                        },
                        singleLine = true,
                        supportingText = {
                            Text(
                                text = "${viewModel.email.length}/35"
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        )
                    )

                    Text(
                        text = "Mobile No.",
                        textAlign = TextAlign.Left,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        value = viewModel.mobileNumber,
                        placeholder = {
                            Text("XXXXXXXXXX")
                        },
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            if (it.length <= 10) {
                                viewModel.mobileNumber = it
                            }
                        },
                        prefix = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextFieldSeparator()
                                /*Text(text = "+91 ")*/
                            }
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
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(22.dp),
                                painter = painterResource(
                                    id = R.drawable.icon_call
                                ),
                                contentDescription = null
                            )
                        },
                        supportingText = {
                            Text(
                                text = "${viewModel.mobileNumber.length}/10"
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Text(
                        text = "Password",
                        textAlign = TextAlign.Left,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        value = viewModel.password,
                        placeholder = {
                            Text("**********")
                        },
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            if (it.length <= 10) {
                                viewModel.password = it
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        singleLine = true,
                        supportingText = {
                            Text(
                                text = "${viewModel.password.length}/10"
                            )
                        },
                        visualTransformation = if (viewModel.keyVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.keyVisible = !viewModel.keyVisible
                                }) {
                                Icon(
                                    imageVector = if (viewModel.keyVisible)
                                        Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff, contentDescription = null
                                )
                            }
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
                        leadingIcon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.icon_passlock
                                ),
                                contentDescription = null
                            )
                        },
                        prefix = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextFieldSeparator()
                            }
                        },
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            viewModel.validateAndInsert()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Save Password",
                            fontSize = 22.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (viewModel.success.value) {
                        LaunchedEffect(Unit) {
                            navigator.navigate(PasswordsScreenDestination)
                        }
                    }
                }
            }
        }
    }
    BackHandler {
        navigator.navigate(PasswordsScreenDestination)
    }
}

@Composable
fun TextFieldSeparator() {
    Box(
        modifier = Modifier
            .padding(end = 12.dp)
            .height(24.dp)
            .width(1.dp)
            .background(color = Color.LightGray)
    )
}

/*
@Preview(showBackground = true)
@Composable
fun AddPasswordScreenPreview() {
    AddPasswordScreen()
}*/
