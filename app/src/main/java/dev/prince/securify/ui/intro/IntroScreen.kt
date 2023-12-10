package dev.prince.securify.ui.intro

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.net.ConnectivityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.R
import dev.prince.securify.signin.GoogleAuthUiClient
import dev.prince.securify.ui.auth.NavigationSource
import dev.prince.securify.ui.destinations.MasterKeyScreenDestination
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.LocalSnackbar
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination
@Composable
fun IntroScreen(
    viewModel: IntroViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    val snackbar = LocalSnackbar.current

    LaunchedEffect(Unit) {
        viewModel.messages.collect {
            snackbar(it)
        }
    }

    val context = LocalContext.current

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit) {
        if (googleAuthUiClient.getSignedInUser() != null) {
            navigator.navigate(
                MasterKeyScreenDestination(NavigationSource.INTRO)
            )
        }
    }
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                scope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            viewModel.showSnackBar("Sign in successful")

            navigator.navigate(
                MasterKeyScreenDestination(NavigationSource.INTRO)
            )
            viewModel.resetState()
        }
    }

    IntroScreenContent(
        navigator = navigator,
        googleAuthUiClient = googleAuthUiClient,
        launcher = launcher
    )

    BackHandler {
        (context as ComponentActivity).finish()
    }
}

@Composable
fun IntroScreenContent(
    viewModel: IntroViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    googleAuthUiClient: GoogleAuthUiClient,
    launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = R.drawable.surviellance),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.intro_tagline_1),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = TextStyle(
                    fontSize = 42.sp,
                    fontFamily = poppinsFamily,
                    lineHeight = 42.sp
                )
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.intro_tagline_2),
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(46.dp))

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    if (viewModel.isNetworkConnected(connectivityManager)) {
                        scope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    } else {
                        viewModel.showSnackBar("Not connected to the internet")
                    }
                    /*navigator.navigate(
                        MasterKeyScreenDestination(NavigationSource.INTRO)
                    )*/
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.get_started),
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = poppinsFamily
                    )
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
