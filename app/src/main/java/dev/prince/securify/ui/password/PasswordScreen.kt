package dev.prince.securify.ui.password

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
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import dev.prince.securify.ui.components.SheetSurface
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.Gray
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.LocalSnackbar
import dev.prince.securify.util.clickWithRipple

@Destination
@Composable
fun PasswordScreen(
    navigator: DestinationsNavigator,
    viewModel: PasswordViewModel = hiltViewModel(),
    accountId: Int
) {

    if (accountId != -1) {
        viewModel.isEditScreen = true
    }

    if (viewModel.isEditScreen) {
        LaunchedEffect(Unit) {
            viewModel.getAccountById(accountId)
        }
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
                painter = painterResource(R.drawable.icon_arrow_left),
                tint = Color.White,
                contentDescription = "Go back",
                modifier = Modifier.clickWithRipple {
                    navigator.popBackStack()
                }
            )

            Text(
                modifier = Modifier.padding(
                    top = 18.dp, bottom = 12.dp,
                    start = 16.dp, end = 16.dp
                ),
                text = if (viewModel.isEditScreen) "Edit Password" else "Add New Password",
                color = Color.White,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold
                ),
            )
        }

        SheetSurface(
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

                SearchOutlinedTextFieldWithDropdown()

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    value = viewModel.username,
                    label = {
                        Text(
                            text = "Username",
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
                                id = R.drawable.icon_username
                            ),
                            tint = Color.Gray,
                            contentDescription = null
                        )
                    },
                    prefix = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextFieldSeparator(24)
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

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    value = viewModel.email,
                    label = {
                        Text(
                            text = "Email ID",
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
                            tint = Color.Gray,
                            contentDescription = null
                        )
                    },
                    prefix = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextFieldSeparator(24)
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

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    value = viewModel.mobileNumber,
                    label = {
                        Text(
                            text = "Mobile Number",
                            style = TextStyle(
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
                            TextFieldSeparator(24)
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
                            tint = Color.Gray,
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
                        label = {
                            Text(
                                text = "Password",
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
                                    painter = if (viewModel.keyVisible)
                                        painterResource(R.drawable.icon_visibility)
                                    else painterResource(R.drawable.icon_visibility_off),
                                    tint = Color.Gray,
                                    contentDescription = null
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
                                    id = R.drawable.icon_lock
                                ),
                                tint = Color.Gray,
                                contentDescription = null
                            )
                        },
                        prefix = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextFieldSeparator(24)
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    )
                    Icon(
                        modifier = Modifier
                            .padding(top = 18.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)
                            .size(34.dp)
                            .clickWithRipple {
                                viewModel.generateRandomPassword()
                            },
                        painter = painterResource(R.drawable.icon_regenerate),
                        tint = Color.Black,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(200.dp),
                    value = viewModel.note,
                    label = {
                        Text(
                            text = "Note",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = poppinsFamily,
                                color = Gray
                            )
                        )
                    },
                    maxLines = 6,
                    shape = RoundedCornerShape(8.dp),
                    onValueChange = {
                        if (it.length <= 140) {
                            viewModel.note = it
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
                                id = R.drawable.icon_note
                            ),
                            tint = Color.Gray,
                            contentDescription = null
                        )
                    },
                    prefix = {
                        Row(
                            modifier = Modifier.padding(top = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextFieldSeparator(134)
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${viewModel.note.length}/140"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (viewModel.isEditScreen) {
                                viewModel.validationAndUpdate(accountId)
                            } else {
                                viewModel.validateAndInsert()
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        if (viewModel.isEditScreen) {
                            viewModel.validationAndUpdate(accountId)
                        } else {
                            viewModel.validateAndInsert()
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (viewModel.isEditScreen) "Update Password" else "Save Password",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                if (viewModel.success.value) {
                    LaunchedEffect(Unit) {
                        navigator.popBackStack()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchOutlinedTextFieldWithDropdown(
    viewModel: PasswordViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current

//    Adjustment for dropdown height to display
//    single item in dropdown instead of empty list space.

    val dropDownMenuVerticalPadding = 8.dp
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
        var sum = with(density) { dropDownMenuVerticalPadding.toPx().toInt() } * 2
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
            label = {
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

@Composable
fun TextFieldSeparator(
    height: Int
) {
    Box(
        modifier = Modifier
            .padding(end = 12.dp)
            .height(height.dp)
            .width(1.dp)
            .background(color = Color.LightGray),
        contentAlignment = Alignment.Center
    ) {}
}