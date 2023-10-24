package dev.prince.securify.ui.add

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.R
import dev.prince.securify.ui.composables.BottomSheetSurface
import dev.prince.securify.ui.destinations.PasswordsScreenDestination
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.Gray
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.LocalSnackbar
import dev.prince.securify.util.clickWithRipple

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun AddScreen(
    navigator: DestinationsNavigator,
    viewModel: AddViewModel = hiltViewModel()
) {

    val snackbar = LocalSnackbar.current

    LaunchedEffect(Unit) {
        viewModel.messages.collect {
            snackbar(it)
        }
    }

    Column(
        modifier = Modifier
            .background(color = BgBlack)
    ) {

        val focusManager = LocalFocusManager.current

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                tint = Color.White,
                contentDescription = "Go back",
                modifier = Modifier.clickWithRipple {
                    navigator.navigate(PasswordsScreenDestination)
                }
            )

            Text(
                modifier = Modifier.padding(
                    top = 18.dp, bottom = 12.dp,
                    start = 16.dp, end = 16.dp
                ),
                text = "Add New Password",
                color = Color.White,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = poppinsFamily
                ),
                fontWeight = FontWeight.SemiBold,
            )
        }

        BottomSheetSurface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Account Name",
                    textAlign = TextAlign.Left,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    ),
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                SearchOutlinedTextFieldWithDropdown()

                Text(
                    text = "Username",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    value = viewModel.username,
                    placeholder = {
                        Text(
                            "John Doe",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = poppinsFamily,
                                color = Gray
                            )
                        )
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
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )

                Text(
                    text = "Email ID",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    value = viewModel.email,
                    placeholder = {
                        Text(
                            "john@example.com",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = poppinsFamily,
                                color = Gray
                            )
                        )
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
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )

                Text(
                    text = "Mobile No.",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    value = viewModel.mobileNumber,
                    placeholder = {
                        Text(
                            "XXXXXXXXXX", style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = poppinsFamily,
                                color = Gray
                            )
                        )
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
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )

                Text(
                    text = "Password",
                    textAlign = TextAlign.Left,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {

                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 6.dp),
                        value = viewModel.password,
                        placeholder = {
                            Text(
                                " ∗ ∗ ∗ ∗ ∗ ∗ ∗ ∗ ∗ ∗ ",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    color = Gray
                                )
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            if (it.length <= 25) {
                                viewModel.password = it
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        singleLine = true,
                        supportingText = {
                            Text(
                                text = "${viewModel.password.length}/25"
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
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.validateAndInsert()
                            }
                        )
                    )
                    Icon(
                        modifier = Modifier
                            .padding(top = 14.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)
                            .size(34.dp)
                            .clickWithRipple {
                                viewModel.generateRandomPassword()
                            },
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }

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
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                if (viewModel.success.value) {
                    LaunchedEffect(Unit) {
                        navigator.navigate(PasswordsScreenDestination)
                    }
                }
            }
        }
        BackHandler {
            navigator.navigate(PasswordsScreenDestination)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchOutlinedTextFieldWithDropdown(
    viewModel: AddViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        expanded = viewModel.suggestions.isNotEmpty(),
        onExpandedChange = { viewModel.resetSuggestions() },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = viewModel.accountName,
            placeholder = {
                Text(
                    "Type Account Name ...",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        color = Gray
                    )
                )
            },
            onValueChange = {
                viewModel.accountName = it
                viewModel.filter(it)
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.expanded) },
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
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        DropdownMenu(
            modifier = Modifier
                .background(Color.White)
                .height(280.dp)
                .exposedDropdownSize(true),
            properties = PopupProperties(focusable = false),
            expanded = viewModel.suggestions.isNotEmpty(),
            onDismissRequest = { viewModel.resetSuggestions() },
        ) {
            viewModel.suggestions.forEach { selectedAccountName ->
                DropdownMenuItem(
                    text = { Text(selectedAccountName) },
                    onClick = {
                        viewModel.accountName = selectedAccountName
                        viewModel.resetSuggestions()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
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