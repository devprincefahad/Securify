package dev.prince.securify.ui.home

import MultiFloatingActionButton
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import dev.prince.securify.database.CardEntity
import dev.prince.securify.ui.components.AlertDialogContent
import dev.prince.securify.ui.components.BottomSheet
import dev.prince.securify.ui.components.SheetSurface
import dev.prince.securify.ui.components.fab.FabButtonItem
import dev.prince.securify.ui.components.fab.FabButtonMain
import dev.prince.securify.ui.destinations.CardScreenDestination
import dev.prince.securify.ui.destinations.PasswordScreenDestination
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.Gray
import dev.prince.securify.ui.theme.LightBlack
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.AccountOrCard
import dev.prince.securify.util.LocalSnackbar
import dev.prince.securify.util.cardSuggestions
import dev.prince.securify.util.suggestionsWithImages
import kotlinx.coroutines.delay

@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    var searchQuery by rememberSaveable { mutableStateOf("") }

    val combinedData by viewModel.combinedData.collectAsState(emptyList())

    val isCombinedDataEmpty = remember { derivedStateOf { combinedData.isEmpty() } }

    var isLoading by remember { mutableStateOf(true) }

    val snackbar = LocalSnackbar.current

    LaunchedEffect(Unit) {
        viewModel.messages.collect {
            snackbar(it)
        }
    }

    LaunchedEffect(true) {
        delay(400)
        isLoading = false
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

    if (viewModel.showAccountDeleteDialog) ConfirmAccountDeletionDialog()

    if (viewModel.showCardDeleteDialog) ConfirmCardDeletionDialog()

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisible.value,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                val items = listOf(
                    FabButtonItem(R.drawable.icon_card, label = "Add Card"),
                    FabButtonItem(R.drawable.icon_pass, label = "Add Password")
                )
                val fabIcon = FabButtonMain(R.drawable.icon_add)
                MultiFloatingActionButton(
                    items = items,
                    fabIcon = fabIcon,
                    onFabItemClicked = {
                        when (it.label) {
                            "Add Card" -> navigator.navigate(CardScreenDestination(-1))
                            "Add Password" -> navigator.navigate(PasswordScreenDestination(-1))
                            else -> {
                                // To handle other cases if needed
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        var showSheet by remember { mutableStateOf(false) }

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
                text = "My Vault",
                color = Color.White,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold
                )
            )

            SheetSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        OutlinedTextField(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp, end = 6.dp,
                                    top = 16.dp, bottom = 8.dp
                                )
                                .weight(1f),
                            value = searchQuery,
                            onValueChange = {
                                if (it.length <= 25) {
                                    searchQuery = it
                                    viewModel.setSearchQuery(it)
                                }
                            },
                            placeholder = {
                                Text(
                                    "Search in Vault",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.icon_search),
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

                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 6.dp, end = 16.dp,
                                    top = 16.dp, bottom = 8.dp
                                )
                                .size(54.dp)
                                .background(Blue, shape = RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    showSheet = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                painter = painterResource(R.drawable.icon_filter),
                                contentDescription = "Filter Icon",
                                tint = Color.White
                            )
                        }
                    }

                    if (showSheet) {
                        BottomSheet(
                            onDismiss = { showSheet = false },
                            content = {
                                LazyColumn {
                                    items(viewModel.filterOptions) { option ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.selectedOption = option
                                                    searchQuery = ""
                                                    viewModel.setSearchQuery("")
                                                    showSheet = false
                                                }
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val isSelected = viewModel.selectedOption == option
                                            val icon =
                                                if (isSelected) R.drawable.icon_selected else R.drawable.icon_unselected

                                            Icon(
                                                painter = painterResource(icon),
                                                contentDescription = "Option Icon",
                                                tint = Blue
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Text(
                                                text = option,
                                                fontSize = 16.sp,
                                                style = TextStyle(
                                                    fontSize = 16.sp,
                                                    fontFamily = poppinsFamily,
                                                    fontWeight = FontWeight.Medium
                                                ),
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.navigationBarsPadding())
                                Spacer(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .fillMaxWidth()
                                        .background(color = BgBlack)
                                )
                            }
                        )
                    }
                    if (isLoading) {
                        ColumnProgressIndicator()
                    } else if (isCombinedDataEmpty.value) {
                        EmptyListPlaceholder(
                            searchQuery.isNotEmpty(),
                            viewModel.selectedOption
                        )
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

                            items(combinedData) { item ->
                                when (item) {
                                    is AccountOrCard.AccountItem -> {
                                        if (viewModel.selectedOption == "All" || viewModel.selectedOption == "Passwords") {
                                            AccountRow(navigator, item.account)
                                        }
                                    }

                                    is AccountOrCard.CardItem -> {
                                        if (viewModel.selectedOption == "All" || viewModel.selectedOption == "Cards") {
                                            CardRow(navigator, item.card)
                                        }
                                    }
                                }
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

@Composable
fun AccountRow(
    navigator: DestinationsNavigator,
    account: AccountEntity,
    viewModel: HomeViewModel = hiltViewModel()
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

        var expanded by remember { mutableStateOf(false) }

        val clipboardManager: ClipboardManager = LocalClipboardManager.current

        val matchingImage =
            suggestionsWithImages.firstOrNull { it.first == account.accountName }?.second

        val painter = matchingImage ?: R.drawable.icon_others
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Image(
                painter = painterResource(painter),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )

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
                    account.email.isNotBlank() -> viewModel.decryptInput(account.email)
                    account.userName.isNotBlank() -> viewModel.decryptInput(account.userName)
                    account.mobileNumber.isNotBlank() -> viewModel.decryptInput(account.mobileNumber)
                    else -> ""
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
                        val password = viewModel.decryptInput(account.password)
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
                                .size(26.dp),
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
                                navigator.navigate(PasswordScreenDestination(account.id))
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(R.drawable.icon_edit),
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
                                viewModel.onUserAccountDeleteClick(account)
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(R.drawable.icon_delete),
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

@Composable
fun CardRow(
    navigator: DestinationsNavigator,
    card: CardEntity,
    viewModel: HomeViewModel = hiltViewModel()
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

        var expanded by remember { mutableStateOf(false) }

        val clipboardManager: ClipboardManager = LocalClipboardManager.current

        val matchingImage =
            cardSuggestions.firstOrNull { it.first == card.cardProvider }?.second

        val painter = matchingImage ?: R.drawable.icon_card

        val tint = if (painter == R.drawable.icon_card) {
            Gray
        } else {
            Color.Unspecified
        }

        val decryptedCardNumber = viewModel.decryptInput(card.cardNumber)
        val cardNumber = decryptedCardNumber.takeLast(4).padStart(decryptedCardNumber.length, '*')

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp, end = 12.dp,
                    top = 12.dp, bottom = 12.dp
                )
        ) {

            Icon(
                painter = painterResource(painter),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp),
                tint = tint
            )

            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {

                Text(
                    text = viewModel.decryptInput(card.cardHolderName),
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = cardNumber,
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
                        clipboardManager.setText(
                            AnnotatedString(viewModel.decryptInput(card.cardNumber))
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
                                .size(26.dp),
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
                                //add edit screen navigation
                                navigator.navigate(CardScreenDestination(card.id))
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(R.drawable.icon_edit),
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
                                viewModel.onUserCardDeleteClick(card)
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(R.drawable.icon_delete),
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

@Composable
private fun ConfirmAccountDeletionDialog(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    AlertDialogContent(
        onDismissRequest = {
            viewModel.showAccountDeleteDialog = false
        },
        onConfirmation = {
            viewModel.deleteAccount()
        },
        dialogTitle = "Delete Password?",
        dialogText = "Are you sure you want to delete password for ${viewModel.accountToDelete.accountName}?",
        confirmTitle = "Delete"
    )
}

@Composable
private fun ConfirmCardDeletionDialog(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val name = viewModel.cardToDelete.cardHolderName
    val decryptedName = viewModel.decryptInput(name)

    AlertDialogContent(
        onDismissRequest = {
            viewModel.showCardDeleteDialog = false
        },
        onConfirmation = {
            viewModel.deleteCard()
        },
        dialogTitle = "Delete Card?",
        dialogText = "Are you sure you want to delete ${decryptedName}'s Card?",
        confirmTitle = "Delete"
    )
}

@Composable
fun EmptyListPlaceholder(
    isSearch: Boolean,
    selectedOption: String
) {

    if (isSearch) {
        CommonColumnPlaceHolder(text = "Sorry, Nothing found!")
    } else {
        when (selectedOption) {
            "Passwords" -> CommonColumnPlaceHolder(text = "No Passwords found!")
            "Cards" -> CommonColumnPlaceHolder(text = "No Cards found!")
            else -> CommonColumnPlaceHolder(text = "Vault is empty")
        }
    }
}

@Composable
fun CommonColumnPlaceHolder(
    text: String
) {
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
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun ColumnProgressIndicator(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Blue,
            modifier = Modifier
                .size(36.dp)
        )
    }
}