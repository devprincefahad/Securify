package dev.prince.securify.ui.components.fab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Represents the state of a Floating Action Button (FAB), which can be either Collapsed or Expanded.
 * The FAB state is used to determine its visibility and behavior, such as showing or hiding sub-items.
 */
sealed class FabButtonState {
    object Collapsed : FabButtonState()
    object Expand : FabButtonState()

    fun isExpanded() = this == Expand

    fun toggleValue() = if (isExpanded()) {
        Collapsed
    } else {
        Expand
    }
}

/**
 * Remembers the state of a Multi-Floating Action Button (FAB) using [remember] and [mutableStateOf].
 *
 * @return A [MutableState] that holds the current state of the Multi-FAB.
 */
@Composable
fun rememberMultiFabState() =
    remember { mutableStateOf<FabButtonState>(FabButtonState.Collapsed) }