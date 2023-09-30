package dev.prince.securify.presentation.add_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import dev.prince.securify.R
import dev.prince.securify.presentation.home.HomeScreen
import dev.prince.securify.ui.theme.LightBlue
import dev.prince.securify.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewPasswordScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Text(
                        text = "Add a new Password",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                }
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
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

            var expanded by remember { mutableStateOf(false) }
            var selectedOption : Pair<String, Painter>? by remember { mutableStateOf(null) }
            var dropDownWidth by remember { mutableStateOf(0) }

            if (selectedOption != null) {
                Image(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .height(160.dp)
                        .fillMaxWidth(),
                    painter = selectedOption!!.second,
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
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .onSizeChanged {
                            dropDownWidth = it.width
                        }
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedOption?.first ?: "Choose an account",
                    onValueChange = { },
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    }
                )
                ExposedDropdownMenu(
                    modifier = Modifier
                        .width(with(LocalDensity.current){dropDownWidth.toDp()})
                        .height(280.dp)
                        .background(Color.White),
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    optionsWithImages.forEach { (selectionOption,painter) ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOption = selectionOption to painter
                                expanded = false
                            },
                            text = {
                                Text(text = selectionOption)
                            }
                        )
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
                value = "",
                placeholder = {
                    Text("Your Username")
                },
                shape = RoundedCornerShape(8.dp),
                onValueChange = {

                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
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
                value = "",
                placeholder = {
                    Text("Your Email ID")
                },
                shape = RoundedCornerShape(8.dp),
                onValueChange = {

                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            Text(
                text = "Password",
                textAlign = TextAlign.Left,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            val key by rememberSaveable { mutableStateOf("") }
            var keyVisible by rememberSaveable { mutableStateOf(false) }

            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                value = "",
                placeholder = {
                    Text("Your Password")
                },
                shape = RoundedCornerShape(8.dp),
                onValueChange = {

                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                trailingIcon = {
                    val image = if (keyVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    // Please provide localized description for accessibility services
                    val description = if (keyVisible) "Hide password" else "Show password"

                    IconButton(onClick = { keyVisible = !keyVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = { /* Do something */ },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlue,
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddNewPasswordScreenPreview() {
    AddNewPasswordScreen()
}