package dev.prince.securify.ui.passwords

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.ui.destinations.AddPasswordScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun PasswordsScreen(
    navigator: DestinationsNavigator,
    viewModel: PasswordsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val accounts = viewModel.accounts.collectAsState(emptyList())
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "My Passwords"
                    )
                }
            )
        },
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
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
    BackHandler {
        (context as ComponentActivity).finish()
    }

}