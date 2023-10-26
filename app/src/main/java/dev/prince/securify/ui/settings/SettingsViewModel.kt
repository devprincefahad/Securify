package dev.prince.securify.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.local.SharedPrefHelper
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: SharedPrefHelper
) : ViewModel() {

    var checked by mutableStateOf(prefs.getSwitchState())

    fun setSwitchState(checked: Boolean) {
        prefs.setSwitchState(checked)
    }

}