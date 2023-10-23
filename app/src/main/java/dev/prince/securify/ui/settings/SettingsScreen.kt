package dev.prince.securify.ui.settings

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.ui.auth.NavigationSource
import dev.prince.securify.ui.destinations.MasterKeyScreenDestination
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Gray
import dev.prince.securify.ui.theme.poppinsFamily

data class SettingsItem(
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator
) {

    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BgBlack),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(top = 18.dp, bottom = 12.dp),
            text = "Settings",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFamily
        )

        val items = remember { listOf(
            SettingsItem(
                "Update Master Key",
                Icons.Outlined.LockOpen,
                onClick = { navigator.navigate(MasterKeyScreenDestination(NavigationSource.SETTINGS)) }
            ),
            SettingsItem(
            "Share Securify",
                Icons.Outlined.Share,
                onClick = { /* TODO hook up Firebase Remote Config for Share Securify  */ }
            ),
            SettingsItem(
                "About Securify",
                Icons.Outlined.Info,
                onClick = { showSheet = true }
            )
        ) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(
                        topStart = 24.dp, topEnd = 24.dp
                    )
                ),
            shape = RoundedCornerShape(
                bottomStart = 0.dp, bottomEnd = 0.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxSize()
            ) {
                items.forEach {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Gray
                        ),
                        shape = RoundedCornerShape(20.dp),
                        onClick = it.onClick
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = it.text,
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
        if (showSheet) {
            AboutBottomSheet {
                showSheet = false
            }
        }
    }
    BackHandler {
        (context as ComponentActivity).finish()
    }
}
