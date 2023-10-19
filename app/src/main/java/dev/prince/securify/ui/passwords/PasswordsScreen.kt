package dev.prince.securify.ui.passwords

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.R
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.ui.destinations.AddPasswordScreenDestination
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.LightBlack
import dev.prince.securify.ui.theme.poppinsFamily

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun PasswordsScreen(
    navigator: DestinationsNavigator,
    viewModel: PasswordsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val accounts = viewModel.accounts.collectAsState(emptyList())
    val snackbarHostState = remember { SnackbarHostState() }

    // State for holding the search query
    var searchQuery by remember { mutableStateOf("") }

    // Filtered accounts based on search query
    val filteredAccounts = accounts.value.filter { account ->
        account.accountName.contains(searchQuery, ignoreCase = true) ||
                account.userName.contains(searchQuery, ignoreCase = true) ||
                account.email.contains(searchQuery, ignoreCase = true) ||
                account.mobileNumber.contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        viewModel.messages.collect {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navigator.navigate(AddPasswordScreenDestination)
                },
                containerColor = Blue,
                contentColor = Color.White,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(text = "Add New Password") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier.padding(
                    top = 18.dp, bottom = 12.dp
                ),
                text = "My Passwords",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFamily
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
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

                // Search Bar
                OutlinedTextField(
                    modifier = Modifier
                        .padding(
                            start = 16.dp, end = 16.dp,
                            top = 16.dp, bottom = 8.dp
                        )
                        .fillMaxWidth(),
                    value = searchQuery,
                    onValueChange = {
                        if (it.length <= 25) {
                            searchQuery = it
                        }
                    },
                    placeholder = {
                        Text("Search Passwords")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = LightBlack,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.Gray
                    )
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(filteredAccounts) { account ->
                        AccountRow(account, viewModel)
                    }
                }
            }
        }
    }
    BackHandler {
        (context as ComponentActivity).finish()
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AccountRow(
    account: AccountEntity,
    viewModel: PasswordsViewModel
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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

        val selectedOption =
            optionsWithImages.find { it.first.equals(account.accountName, ignoreCase = true) }

        var expanded by remember { mutableStateOf(false) }

        val clipboardManager: ClipboardManager = LocalClipboardManager.current

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Image
            selectedOption?.second?.let {
                Image(
                    painter = it,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }

            // Account Name and Details
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {
                Text(
                    text = account.accountName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                val displayInfo = when {
                    !account.email.isNullOrBlank() -> account.email
                    !account.userName.isNullOrBlank() -> account.userName
                    else -> account.mobileNumber
                }

                Text(
                    text = displayInfo,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = 4.dp)
            ) {

                IconButton(
                    onClick = {
                        val password = viewModel.decryptPassword(account.password)
                        clipboardManager.setText(
                            AnnotatedString(password)
                        )
                        viewModel.showCopyMsg()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null
                    )
                }

                Box(
                    modifier = Modifier.size(48.dp)
                ) {
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options Icon"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                    ) {

                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                // Add edit functionality here
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Icon"
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                viewModel.deleteAccount(account)
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
