package dev.prince.securify.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.database.SecurifyDatabase
import dev.prince.securify.encryption.EncryptionManager
import dev.prince.securify.util.oneShotFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: SecurifyDatabase,
    private val encryptionManager: EncryptionManager
) : ViewModel() {

    val accounts = db.accountDao().getAllAccounts()

    val messages = oneShotFlow<String>()

    var showDialog by mutableStateOf(false)

    var accountToDelete by mutableStateOf(AccountEntity(-1, "", "", "", "", "", ""))

    fun onUserDeleteClick(accountEntity: AccountEntity) {
        accountToDelete = accountEntity
        showDialog = true
    }

    fun deleteAccount() {
        viewModelScope.launch {
            db.accountDao().deleteAccount(accountToDelete)
            showDialog = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptPassword(password: String): String {
        return encryptionManager.decrypt(password)
    }

    fun showCopyMsg() {
        messages.tryEmit("Password copied to clipboard.")
    }

}