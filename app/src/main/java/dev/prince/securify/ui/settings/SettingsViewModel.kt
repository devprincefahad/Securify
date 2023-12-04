package dev.prince.securify.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.AccountDao
import dev.prince.securify.database.CardDao
import dev.prince.securify.local.SharedPrefHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dbAccount: AccountDao,
    private val dbCard: CardDao,
    private val prefs: SharedPrefHelper
) : ViewModel() {

    var checked by mutableStateOf(prefs.getSwitchState())

    var showAllDataDeleteDialog by mutableStateOf(false)

    fun setSwitchState(checked: Boolean) {
        prefs.setSwitchState(checked)
    }

    fun deleteAll() {
        prefs.resetMasterKeyAndSwitch()
        viewModelScope.launch(Dispatchers.IO) {
            dbAccount.deleteAllAccounts()
            dbCard.deleteAllCards()
        }
    }

}