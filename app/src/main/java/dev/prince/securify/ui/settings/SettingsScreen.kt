package dev.prince.securify.ui.settings

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun SettingsScreen(

){

    val context = LocalContext.current

    Column {
        Text(
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            ),
            text = "Settings screen",
            fontSize = 26.sp
        )
    }
    BackHandler {
        (context as ComponentActivity).finish()
    }
}