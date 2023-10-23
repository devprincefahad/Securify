package dev.prince.securify.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import dev.prince.securify.ui.bottomnav.BottomBar
import dev.prince.securify.ui.theme.SecurifyTheme
import dev.prince.securify.util.LocalSnackbar
import dev.prince.securify.util.shouldShowBottomBar
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecurifyTheme {

                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()
                val destination = navController.appCurrentDestinationAsState().value
                    ?: NavGraphs.root.startRoute.startAppDestination

                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                val onSnackbarMessageReceived = fun(message: String) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (destination.shouldShowBottomBar()) {
                            BottomBar(navController)
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(snackbarHostState)
                    }
                ) { contentPadding ->
                    CompositionLocalProvider(
                        LocalSnackbar provides onSnackbarMessageReceived
                    ) {
                        DestinationsNavHost(
                            modifier = Modifier
                                .padding(contentPadding),
                            navGraph = NavGraphs.root,
                            navController = navController,
                            engine = engine
                        )
                    }
                }
            }
        }
    }
}

