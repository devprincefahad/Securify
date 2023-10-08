package dev.prince.securify.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import dev.prince.securify.ui.NavGraphs
import dev.prince.securify.ui.appCurrentDestinationAsState
import dev.prince.securify.ui.destinations.Destination
import dev.prince.securify.ui.destinations.IntroScreenDestination
import dev.prince.securify.ui.destinations.SetupKeyScreenDestination
import dev.prince.securify.ui.destinations.UnlockScreenDestination
import dev.prince.securify.ui.startAppDestination
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

fun <T> oneShotFlow() = MutableSharedFlow<T>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

fun Destination.shouldShowBottomBar(): Boolean {

    return (this !in listOf(
        IntroScreenDestination,
        UnlockScreenDestination,
        SetupKeyScreenDestination
    ))
}