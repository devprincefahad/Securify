package dev.prince.securify.ui.passwords

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.R
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.ui.add.TextFieldSeparator
import dev.prince.securify.ui.destinations.AddPasswordScreenDestination
import dev.prince.securify.ui.theme.LightBlack
import dev.prince.securify.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
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
                account.mobileNumber.contains(searchQuery, ignoreCase = true) ||
                account.password.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navigator.navigate(AddPasswordScreenDestination)
                },
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
                    top = 28.dp,
                    bottom = 8.dp
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
                        if (it.length <= 25){
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
                        unfocusedBorderColor = LightBlack,
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
                        AccountRow(account)
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
fun AccountRow(account: AccountEntity) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(account.accountName)
            Text(account.userName)
            Text(account.email)
            Text(account.mobileNumber)
            Text(account.password)
        }
    }
}