package dev.prince.securify.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {

    ModalBottomSheet(
        containerColor = Color(0xFFFFFFFF),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            onDismiss()
        },
        dragHandle = {
            BottomSheetDefaults.DragHandle()
        },
        content = {
            content()
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    )

    BackHandler(
        onBack = {
            onDismiss()
        }
    )
}