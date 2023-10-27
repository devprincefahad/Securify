package dev.prince.securify.ui.bottomnav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import dev.prince.securify.R
import dev.prince.securify.ui.destinations.GenerateScreenDestination
import dev.prince.securify.ui.destinations.HomeScreenDestination
import dev.prince.securify.ui.destinations.SettingsScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Home(HomeScreenDestination, Icons.Default.Home, R.string.home),
    Generate(
        GenerateScreenDestination,
        Icons.Default.Key,
        R.string.generate
    ),
    Settings(SettingsScreenDestination, Icons.Default.Settings, R.string.settings)
}