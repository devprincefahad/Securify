package dev.prince.securify.ui.edit

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ChevronLeft
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.ui.add.TextFieldSeparator
import dev.prince.securify.ui.composables.BottomSheetSurface
import dev.prince.securify.ui.destinations.PasswordsScreenDestination
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.clickWithRipple

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun EditScreen(
    navigator: DestinationsNavigator,
    viewModel: EditViewModel = hiltViewModel(),
    accountId: Int
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
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
                .padding(innerPadding)
                .background(color = Color.Black)
        ) {

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
                    text = "Edit Password",
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
                        .verticalScroll(state = scrollState)
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {

                    val account by viewModel.getAccountById(accountId)
                        .collectAsState(
                            initial = AccountEntity(
                                id = 0, accountName = "",
                                userName = "", email = "",
                                mobileNumber = "", password = ""
                            )
                        )

                    var accountName by remember(account.accountName) { mutableStateOf(account.accountName) }
                    var userName by remember(account.userName) { mutableStateOf(account.userName) }
                    var email by remember(account.email) { mutableStateOf(account.email) }
                    var mobileNumber by remember(account.mobileNumber) { mutableStateOf(account.mobileNumber) }
                    val decryptPassword = viewModel.decryptPassword(account.password)
                    var password by remember(decryptPassword) { mutableStateOf(decryptPassword) }

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

                    val suggestions = listOf(
                        "Instagram",
                        "Facebook",
                        "LinkedIn",
                        "Snapchat",
                        "YouTube",
                        "Netflix",
                        "Discord",
                        "Twitter",
                        "Amazon Prime",
                        "Spotify",
                        "Gmail",
                        "Reddit",
                        "Quora",
                        "Pinterest"
                    )

                    ExposedDropdownMenuBox(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        expanded = viewModel.expanded,
                        onExpandedChange = { viewModel.expanded = !viewModel.expanded },
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            value = accountName,
                            placeholder = {
                                Text("Type Account Name ...")
                            },
                            onValueChange = {
                                if (it.length <= 35) {
                                    accountName = it
                                }
                            },
                            supportingText = {
                                Text(text = "${accountName.length}/35")
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
                            )
                        )
                        // filter options based on text field value
                        val filteringOptions =
                            suggestions.filter { it.contains(accountName, ignoreCase = true) }
                        if (filteringOptions.isNotEmpty()) {
                            DropdownMenu(
                                modifier = Modifier
                                    .background(Color.White)
                                    .height(280.dp)
                                    .exposedDropdownSize(true),
                                properties = PopupProperties(focusable = false),
                                expanded = viewModel.expanded,
                                onDismissRequest = { viewModel.expanded = false },
                            ) {
                                filteringOptions.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            accountName = selectionOption
                                            viewModel.expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
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
                        value = userName,
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            if (it.length <= 35) {
                                userName = it
                            }
                        },
                        supportingText = {
                            Text(text = "${userName.length}/35")
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
                        value = email,
                        placeholder = {
                            Text("john@example.com")
                        },
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            if (it.length <= 35) {
                                email = it
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
                                text = "${email.length}/35"
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
                        value = mobileNumber,
                        placeholder = {
                            Text("XXXXXXXXXX")
                        },
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            if (it.length <= 10) {
                                mobileNumber = it
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
                                text = "${mobileNumber.length}/10"
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
                        value = password,
                        placeholder = {
                            Text("**********")
                        },
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = {
                            if (it.length <= 25) {
                                password = it
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        singleLine = true,
                        supportingText = {
                            Text(
                                text = "${password.length}/25"
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
                            val encryptPassword = viewModel.encryptPassword(password)
                            val updateAccount = AccountEntity(
                                id = accountId,
                                accountName = accountName,
                                userName = userName,
                                email = email,
                                mobileNumber = mobileNumber,
                                password = encryptPassword
                            )
                            viewModel.updateAccountDetails(updateAccount)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Update Password",
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
        BackHandler {
            navigator.navigate(PasswordsScreenDestination)
        }
    }
}