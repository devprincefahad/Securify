package dev.prince.securify.ui.edit_password

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.AccountDao
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.encryption.EncryptionManager
import dev.prince.securify.util.accountSuggestions
import dev.prince.securify.util.generatePassword
import dev.prince.securify.util.getRandomNumber
import dev.prince.securify.util.oneShotFlow
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
    var note by mutableStateOf("")

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
    fun validationAndUpdateDetails(id: Int) {
        if (validateFields()) {
            viewModelScope.launch {
                val currentTimeInMillis = System.currentTimeMillis()
                val accountEntity = AccountEntity(
                    id,
                    accountName.trim(),
                    userName.trim(),
                    email.trim(),
                    mobileNumber,
                    encryptPassword(password.trim()),
                    note.trim(),
                    currentTimeInMillis
                )
                db.updateAccount(accountEntity)
                messages.tryEmit("Successfully Updated!")
                success.value = true
            }
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

    private fun validateFields(): Boolean {
        if (accountName.isBlank()) {
            messages.tryEmit("Please provide an account name")
            return false
        }
        if (userName.isEmpty() && email.isBlank() && mobileNumber.isBlank()) {
            messages.tryEmit("Please provide a username, email, or mobile number")
            return false
        }
        if (password.isBlank()) {
            messages.tryEmit("Password cannot be empty")
            return false
        }
        if (password.trim().isEmpty() || password.contains("\\s+".toRegex())) {
            messages.tryEmit("Password cannot contain whitespace")
            return false
        }
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            messages.tryEmit("Invalid email address")
            return false
        }
        if (!mobileNumber.isDigitsOnly()) {
            messages.tryEmit("Invalid mobile number")
            return false
        }
        return true
    }


}