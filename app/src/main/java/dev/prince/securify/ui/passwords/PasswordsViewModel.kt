package dev.prince.securify.ui.passwords

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.database.SecurifyDatabase
import dev.prince.securify.encryption.EncryptionManager
import dev.prince.securify.util.oneShotFlow
import kotlinx.coroutines.launch
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class PasswordsViewModel @Inject constructor(
    private val db: SecurifyDatabase,
    private val encryptionManager: EncryptionManager
) : ViewModel() {

    val accounts = db.accountDao().getAllAccounts()

    val messages = oneShotFlow<String>()

    var showDialog = mutableStateOf(false)

    fun deleteAccount(account: AccountEntity) {
        viewModelScope.launch {
            db.accountDao().deleteAccount(account)
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