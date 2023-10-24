package dev.prince.securify.ui.passwords

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.R
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.ui.composables.AlertDialogContent
import dev.prince.securify.ui.composables.BottomSheetSurface
import dev.prince.securify.ui.destinations.AddScreenDestination
import dev.prince.securify.ui.destinations.EditScreenDestination
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.LightBlack
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.LocalSnackbar

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun PasswordsScreen(
    navigator: DestinationsNavigator,
    viewModel: PasswordsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val accounts = viewModel.accounts.collectAsState(emptyList())

    // State for holding the search query
    var searchQuery by remember { mutableStateOf("") }

    // Filtered accounts based on search query
    val filteredAccounts = accounts.value.filter { account ->
        account.accountName.contains(searchQuery, ignoreCase = true) ||
                account.userName.contains(searchQuery, ignoreCase = true) ||
                account.email.contains(searchQuery, ignoreCase = true) ||
                account.mobileNumber.contains(searchQuery, ignoreCase = true)
    }

    val snackbar = LocalSnackbar.current
    LaunchedEffect(Unit) {
        viewModel.messages.collect {
            snackbar(it)
        }
    }

    val isVisible = rememberSaveable { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Hide FAB
                if (available.y < -1) {
                    isVisible.value = false
                }

                // Show FAB
                if (available.y > 1) {
                    isVisible.value = true
                }

                return Offset.Zero
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisible.value,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        navigator.navigate(AddScreenDestination)
                    },
                    containerColor = Blue,
                    contentColor = Color.White,
                    icon = {
                        Icon(
                            Icons.Filled.Add,
                            modifier = Modifier.size(26.dp),
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(
                            text = "Add Password",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = BgBlack),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier.padding(
                    top = 18.dp, bottom = 12.dp
                ),
                text = "My Passwords",
                color = Color.White,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold
                )
            )

            BottomSheetSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {

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
                            Text(
                                "Search Passwords",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Normal
                                )
                            )
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

                    if (filteredAccounts.isEmpty()) {
                        EmptyListPlaceholder()
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = 8.dp, end = 8.dp,
                                    top = 8.dp, bottom = 0.dp
                                )
                                .nestedScroll(nestedScrollConnection),
                        ) {
                            items(filteredAccounts) { account ->
                                AccountRow(navigator, account, viewModel)
                            }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AccountRow(
    navigator: DestinationsNavigator,
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

        val suggestionsWithImages = listOf(
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
            "Pinterest" to painterResource(R.drawable.icon_pinterest)
        )

        var expanded by remember { mutableStateOf(false) }

        val clipboardManager: ClipboardManager = LocalClipboardManager.current

        val matchingImage =
            suggestionsWithImages.firstOrNull { it.first == account.accountName }?.second

        val painter = matchingImage ?: painterResource(R.drawable.icon_others)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )

            // Account Name and Details
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {

                Text(
                    text = account.accountName,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold
                    )
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
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
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
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 6.dp),
                        painter = painterResource(R.drawable.icon_copy),
                        contentDescription = "Copy Icon"
                    )
                }

                Box {
                    IconButton(
                        modifier = Modifier
                            .size(26.dp)
                            .padding(end = 4.dp),
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(18.dp),
                            painter = painterResource(R.drawable.icon_more),
                            contentDescription = "Options Icon"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Edit",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            },
                            onClick = {
                                navigator.navigate(EditScreenDestination(account.id))
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Icon"
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete", style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            },
                            onClick = {
                                viewModel.showDialog.value = true
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        )
                    }

                    if (viewModel.showDialog.value) {
                        AlertDialogContent(
                            onDismissRequest = {
                                viewModel.showDialog.value = false
                            },
                            onConfirmation = {
                                viewModel.deleteAccount(account)
                                viewModel.showDialog.value = false
                            },
                            dialogTitle = "Delete Password?",
                            dialogText = "Are you sure you want to delete password for ${account.accountName}?",
                            confirmTitle = "Delete"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyListPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(120.dp),
            painter = painterResource(R.drawable.img_empty_box),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No passwords\nadded yet!",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        )
    }
}
