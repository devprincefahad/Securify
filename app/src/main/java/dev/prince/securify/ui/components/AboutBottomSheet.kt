package dev.prince.securify.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.prince.securify.R
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutBottomSheet(
    onDismiss: () -> Unit
) {
    val modalSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        containerColor = Color(0xFFFFFFFF),
        sheetState = modalSheetState,
        onDismissRequest = {
            onDismiss()
        },
        dragHandle = {
            BottomSheetDefaults.DragHandle()
        },
        content = {
            Column(
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = stringResource(R.string.about_info),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFamily,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.navigationBarsPadding())
                Spacer(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(color = BgBlack)
                )
            }
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    )

    BackHandler(
        onBack = {
            onDismiss()
        }
    )
}