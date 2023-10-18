package dev.prince.securify.ui.auth

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.prince.securify.R
import dev.prince.securify.ui.composables.CreateMasterKeySheetContent
import dev.prince.securify.ui.composables.UpdateMasterKeySheetContent
import dev.prince.securify.ui.destinations.PasswordsScreenDestination
import dev.prince.securify.ui.theme.poppinsFamily

enum class NavigationSource {
    INTRO,
    SETTINGS
}

@Destination
@Composable
fun MasterKeyScreen(
    navigator: DestinationsNavigator,
    navigationSource: NavigationSource,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->

        LaunchedEffect(Unit) {
            viewModel.messages.collect {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
            }
        }
        LaunchedEffect(Unit) {
            viewModel.navigateToHome.collect {
                navigator.navigate(PasswordsScreenDestination) {
                    popUpTo(PasswordsScreenDestination) {
                        inclusive = true
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .padding(contentPadding)
                .fillMaxSize()
                .background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 32.dp),
                text = stringResource(R.string.setup_key_tagline_1),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = TextStyle(
                    fontSize = 52.sp,
                    fontFamily = poppinsFamily,
                    lineHeight = 52.sp
                )
            )
            Spacer(modifier = Modifier.height(60.dp))

            if (navigationSource == NavigationSource.SETTINGS) {
                UpdateMasterKeySheetContent()
            } else {
                CreateMasterKeySheetContent()
            }

            BackHandler {
                if (navigationSource == NavigationSource.INTRO) {
                    (context as ComponentActivity).finish()
                } else {
                    navigator.popBackStack()
                }
            }
        }
    }
}