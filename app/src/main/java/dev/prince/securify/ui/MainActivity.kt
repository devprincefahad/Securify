package dev.prince.securify.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import dev.prince.securify.ui.bottomnav.BottomBar
import dev.prince.securify.ui.theme.SecurifyTheme
import dev.prince.securify.util.shouldShowBottomBar

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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (destination.shouldShowBottomBar()) {
                            BottomBar(navController)
                        }
                    }
                ) { contentPadding ->
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

