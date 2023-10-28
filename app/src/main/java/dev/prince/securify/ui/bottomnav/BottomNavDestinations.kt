package dev.prince.securify.ui.bottomnav

import androidx.annotation.StringRes
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import dev.prince.securify.R
import dev.prince.securify.ui.destinations.GenerateScreenDestination
import dev.prince.securify.ui.destinations.HomeScreenDestination
import dev.prince.securify.ui.destinations.SettingsScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: Int,
    @StringRes val label: Int
) {
    Home(HomeScreenDestination, R.drawable.icon_home, R.string.home),
    Generate(
        GenerateScreenDestination,
        R.drawable.icon_key,
        R.string.generate
    ),
    Settings(SettingsScreenDestination, R.drawable.icon_settings, R.string.settings)
}