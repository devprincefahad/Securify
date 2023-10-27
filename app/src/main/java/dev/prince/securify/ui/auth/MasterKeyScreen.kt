package dev.prince.securify.ui.auth

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.prince.securify.R
import dev.prince.securify.ui.components.CreateMasterKeySheetContent
import dev.prince.securify.ui.components.UpdateMasterKeySheetContent
import dev.prince.securify.ui.destinations.HomeScreenDestination
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.LocalSnackbar

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

    val snackbar = LocalSnackbar.current

    LaunchedEffect(Unit) {
        viewModel.messages.collect {
            snackbar(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToHome.collect {
            navigator.navigate(HomeScreenDestination) {
                popUpTo(HomeScreenDestination) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(color = BgBlack)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 32.dp),
            text = stringResource(R.string.setup_key_tagline_1),
            textAlign = TextAlign.Left,
            color = Color.White,
            style = TextStyle(
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                lineHeight = 60.sp
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

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