package dev.prince.securify.ui.edit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.AccountDao
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.encryption.EncryptionManager
import dev.prince.securify.util.accountSuggestions
import dev.prince.securify.util.generatePassword
import dev.prince.securify.util.getRandomNumber
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
    var suggestions = SnapshotStateList<String>()

    var accountName by mutableStateOf("")
    var userName by mutableStateOf("")
    var email by mutableStateOf("")
    var mobileNumber by mutableStateOf("")
    var password by mutableStateOf("")

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptPassword(password: String): String {
        return encryptionManager.decrypt(password)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAccountById(accountId: Int) {
        viewModelScope.launch {
            db.getAccountById(accountId).collect {
                accountName = it.accountName
                userName = it.userName
                email = it.email
                mobileNumber = it.mobileNumber
                password = decryptPassword(it.password)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptPassword(password: String): String {
        return encryptionManager.encrypt(password)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAccountDetails(id: Int) {
        viewModelScope.launch {
            val accountEntity = AccountEntity(
                id,
                accountName,
                userName,
                email,
                mobileNumber,
                encryptPassword(password)
            )
            db.updateAccount(accountEntity)
            messages.tryEmit("Successfully Updated!")
            success.value = true
        }
    }

    fun filter(accountName: String) {
        suggestions.clear()
        if (accountName.isNotEmpty()) {
            suggestions.addAll(accountSuggestions.filter { it.contains(accountName, true) })
        }
    }

    fun resetSuggestions() {
        suggestions.clear()
    }

    fun generateRandomPassword() {
        password = generatePassword(
            length = getRandomNumber(),
            lowerCase = true,
            upperCase = true,
            digits = true,
            specialCharacters = true
        )
    }

}