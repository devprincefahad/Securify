package dev.prince.securify.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import dev.prince.securify.ui.destinations.AddPasswordScreenDestination
import dev.prince.securify.ui.destinations.Destination
import dev.prince.securify.ui.destinations.EditScreenDestination
import dev.prince.securify.ui.destinations.IntroScreenDestination
import dev.prince.securify.ui.destinations.MasterKeyScreenDestination
import dev.prince.securify.ui.destinations.UnlockScreenDestination
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
        MasterKeyScreenDestination,
        AddPasswordScreenDestination,
        EditScreenDestination
    ))
}

fun Modifier.clickWithRipple(bounded: Boolean = true, onClick: () -> Unit) = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = bounded),
        onClick = { onClick() }
    )
}