package dev.prince.securify.ui.settings

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.google.android.gms.auth.api.identity.Identity
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.securify.singin.GoogleAuthUiClient
import dev.prince.securify.singin.UserData
import dev.prince.securify.ui.auth.NavigationSource
import dev.prince.securify.ui.composables.AboutBottomSheet
import dev.prince.securify.ui.composables.BottomSheetSurface
import dev.prince.securify.ui.destinations.MasterKeyScreenDestination
import dev.prince.securify.ui.theme.BgBlack
import dev.prince.securify.ui.theme.Blue
import dev.prince.securify.ui.theme.LightBlue
import dev.prince.securify.ui.theme.Red
import dev.prince.securify.ui.theme.White
import dev.prince.securify.ui.theme.poppinsFamily
import dev.prince.securify.util.isBiometricSupported
import kotlinx.coroutines.launch

data class SettingsItem(
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    val user = googleAuthUiClient.getSignedInUser()

    val scope = rememberCoroutineScope()

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

        val items = remember {
            listOf(
                SettingsItem(
                    text = "Reset Master Key",
                    icon = Icons.Outlined.LockOpen,
                    onClick = { navigator.navigate(MasterKeyScreenDestination(NavigationSource.SETTINGS)) }
                ),
                if (isBiometricSupported(context)) SettingsItem(
                    text = "Fingerprint Unlock",
                    icon = Icons.Outlined.Fingerprint,
                    onClick = {
                        /**/
                    }
                ) else null,
                SettingsItem(
                    text = "Share",
                    icon = Icons.Outlined.Share,
                    onClick = { /* TODO hook up Firebase Remote Config for Share Securify  */ }
                ),
                SettingsItem(
                    text = "About",
                    icon = Icons.Outlined.Info,
                    onClick = { showSheet = true }
                ),
                SettingsItem(
                    text = "Logout",
                    icon = Icons.Outlined.Logout,
                    onClick = {
                        scope.launch { googleAuthUiClient.signOut() }
                    }
                )
            )
        }

        BottomSheetSurface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 16.dp
                    )
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                UserProfileRow(userData = user)

                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 16.dp,
                            horizontal = 46.dp
                        )
                )

                items.forEach { settingsItem ->
                    settingsItem?.let { SettingsItemRow(it) }
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

@Composable
fun UserProfileRow(userData: UserData?) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            userData?.profilePictureUrl?.replace(
                oldValue = "s96-c", newValue = "s192-c", ignoreCase = true
            )?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 6.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                userData?.username?.let { username ->
                    Text(
                        text = username,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                userData?.email?.let { email ->
                    Text(
                        text = email,
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


@Composable
fun SettingsItemRow(
    settingsItem: SettingsItem,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = settingsItem.onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = settingsItem.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (settingsItem.text == "Logout") Red else Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = settingsItem.text,
                fontSize = 18.sp,
                color = if (settingsItem.text == "Logout") Red else Color.Black,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))

            if (settingsItem.text == "Fingerprint Unlock") {
                Switch(
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 18.dp),
                    checked = viewModel.checked,
                    onCheckedChange = {
                        viewModel.checked = !viewModel.checked
                        viewModel.setSwitchState(viewModel.checked)
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Blue,
                        uncheckedTrackColor = LightBlue,
                        uncheckedBorderColor = LightBlue,
                        uncheckedThumbColor = Color.White
                    )
                )
            }
        }
    }
}
