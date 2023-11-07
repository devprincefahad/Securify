package dev.prince.securify.ui.password

import android.util.Log
import android.util.Patterns
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
class PasswordViewModel @Inject constructor(
    private val db: AccountDao,
    private val encryptionManager: EncryptionManager
) : ViewModel() {

    var isEditScreen by mutableStateOf(false)

    val messages = oneShotFlow<String>()

    var expanded by mutableStateOf(false)
    var accountName by mutableStateOf("")
    var suggestions = SnapshotStateList<String>()

    var username by mutableStateOf("")

    var email by mutableStateOf("")

    var note by mutableStateOf("")

    var mobileNumber by mutableStateOf("")
    var keyVisible by mutableStateOf(false)

    var password by mutableStateOf("")

    val success = mutableStateOf(false)

    fun getAccountById(accountId: Int) {
        viewModelScope.launch {
            db.getAccountById(accountId).collect {
                accountName = it.accountName
                username = encryptionManager.decrypt(it.userName)
                email = encryptionManager.decrypt(it.email)
                mobileNumber = encryptionManager.decrypt(it.mobileNumber)
                password = encryptionManager.decrypt(it.password)
            }
        }
    }

    private fun validateFields(): Boolean {
        if (accountName.isBlank()) {
            messages.tryEmit("Please provide an account name")
            return false
        }
        if (username.isEmpty() && email.isBlank() && mobileNumber.isBlank()) {
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

    fun validateAndInsert() {

        if (validateFields()) {
            val currentTimeInMillis = System.currentTimeMillis()
            val account = AccountEntity(
                id = 0,
                accountName = accountName.trim(),
                userName = encryptionManager.encrypt(username).trim(),
                email = encryptionManager.encrypt(email).trim(),
                mobileNumber = encryptionManager.encrypt(mobileNumber).trim(),
                password = encryptionManager.encrypt(password).trim(),
                note = note.trim(),
                createdAt = currentTimeInMillis
            )

            viewModelScope.launch {
                db.insertAccount(account)
                messages.tryEmit("Credentials Added!")
                success.value = true
            }
        }

    }

    fun validationAndUpdate(id: Int) {
        if (validateFields()) {
            viewModelScope.launch {
                val currentTimeInMillis = System.currentTimeMillis()
                val accountEntity = AccountEntity(
                    id = id,
                    accountName = accountName.trim(),
                    userName = encryptionManager.encrypt(username.trim()),
                    email = encryptionManager.encrypt(email.trim()),
                    mobileNumber = encryptionManager.encrypt(mobileNumber),
                    password = encryptionManager.encrypt(password.trim()),
                    note = note.trim(),
                    createdAt = currentTimeInMillis
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

}