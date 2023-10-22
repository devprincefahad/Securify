package dev.prince.securify.ui.edit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.AccountDao
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.encryption.EncryptionManager
import dev.prince.securify.util.oneShotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val db: AccountDao,
    private val encryptionManager: EncryptionManager
) : ViewModel() {

    val messages = oneShotFlow<String>()

    var expanded by mutableStateOf(false)

    var keyVisible by mutableStateOf(false)

    val success = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptPassword(password: String): String {
        return encryptionManager.decrypt(password)
    }

    fun getAccountById(accountId: Int): Flow<AccountEntity> {
        return db.getAccountById(accountId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptPassword(password: String):String{
        return encryptionManager.encrypt(password)
    }

    fun updateAccountDetails(accountEntity: AccountEntity){
        viewModelScope.launch {
            db.updateAccount(accountEntity)
            success.value = true
        }
    }

}