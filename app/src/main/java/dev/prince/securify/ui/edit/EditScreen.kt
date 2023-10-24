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
import androidx.compose.foundation.layout.requiredSizeIn
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
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
import dev.prince.securify.ui.add.TextFieldSeparator
import dev.prince.securify.ui.composables.BottomSheetSurface
import dev.prince.securify.ui.destinations.PasswordsScreenDestination
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.LocalSnackbar
import dev.prince.securify.util.clickWithRipple

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun EditScreen(
    navigator: DestinationsNavigator,
    viewModel: EditViewModel = hiltViewModel(),
    accountId: Int
) {

    LaunchedEffect(Unit) {
        viewModel.getAccountById(accountId)
    }

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
                    .verticalScroll(state = rememberScrollState())
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
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    value = viewModel.userName,
                    shape = RoundedCornerShape(8.dp),
                    onValueChange = {
                        if (it.length <= 35) {
                            viewModel.userName = it
                        }
                    },
                    supportingText = {
                        Text(text = "${viewModel.userName.length}/35")
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
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )

                Text(
                    text = "Mobile Number",
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
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
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
                            Text("**********")
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
                                viewModel.validationAndUpdateDetails(accountId)
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
                        viewModel.validationAndUpdateDetails(accountId)
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
        BackHandler {
            navigator.navigate(PasswordsScreenDestination)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchOutlinedTextFieldWithDropdown(
    viewModel: EditViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current

//    Adjustment for dropdown height to display
//    single item in dropdown instead of empty list space.

    val DropdownMenuVerticalPadding = 8.dp
    val itemHeights = remember { mutableStateMapOf<Int, Int>() }
    val baseHeight = 330.dp
    val density = LocalDensity.current
    val maxHeight = remember(itemHeights.toMap()) {
        if (itemHeights.keys.toSet() != viewModel.suggestions.indices.toSet()) {
            // if we don't have all heights calculated yet, return default value
            return@remember baseHeight
        }
        val baseHeightInt = with(density) { baseHeight.toPx().toInt() }

        // top+bottom system padding
        var sum = with(density) { DropdownMenuVerticalPadding.toPx().toInt() } * 2
        for ((i, itemSize) in itemHeights.toSortedMap()) {
            sum += itemSize
            if (sum >= baseHeightInt) {
                return@remember with(density) { (sum - itemSize / 2).toDp() }
            }
        }
        // all items fit into base height
        baseHeight
    }

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
                Text("Type Account Name ...")
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
                .exposedDropdownSize(true)
                .requiredSizeIn(maxHeight = maxHeight),
            properties = PopupProperties(focusable = false),
            expanded = viewModel.suggestions.isNotEmpty(),
            onDismissRequest = { viewModel.resetSuggestions() },
        ) {
            viewModel.suggestions.forEachIndexed { index, selectedAccountName ->
                DropdownMenuItem(
                    text = { Text(selectedAccountName) },
                    onClick = {
                        viewModel.accountName = selectedAccountName
                        viewModel.resetSuggestions()
                    },
                    modifier = Modifier.onSizeChanged {
                        itemHeights[index] = it.height
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }

    }
}