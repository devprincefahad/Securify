package dev.prince.securify.ui.add

import android.annotation.SuppressLint
import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.prince.securify.crypto.CryptoManager
import dev.prince.securify.database.AccountDao
import dev.prince.securify.database.AccountEntity
import dev.prince.securify.util.oneShotFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class AddPasswordViewModel @Inject constructor(
    private val db: AccountDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val cryptoManager = CryptoManager()
    val messages = oneShotFlow<String>()

    var expanded by mutableStateOf(false)
    var selectedOption: Pair<String, Painter>? by mutableStateOf(null)
    var textFieldSize by mutableStateOf(Size.Zero)

    var username by mutableStateOf("")

    var email by mutableStateOf("")

    var mobileNumber by mutableStateOf("")
    var keyVisible by mutableStateOf(false)

    var password by mutableStateOf("")
    var otherAccName by mutableStateOf("")

    val success = mutableStateOf(false)

    private fun validateFields() {
        if (username.isEmpty() && email.isBlank() && mobileNumber.isBlank()) {
            messages.tryEmit("Please provide a username, email, or mobile number.")
        }
        /*if (password.length < 6) {
            messages.tryEmit("Password length must be 6 characters or more")
        }*/
        if (selectedOption?.first == null) {
            messages.tryEmit("Please Choose an account.")
        }
        if (selectedOption?.first == "Other" && otherAccName.isBlank()) {
            messages.tryEmit("Please provide other account name")
        }
        if (password.isBlank()) {
            messages.tryEmit("Password cannot be empty")
        }
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            messages.tryEmit("Invalid email address")
        }
    }

    fun validateAndInsert() {
        if ((username.isNotEmpty() || (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email)
                .matches())
                    || mobileNumber.isNotBlank()) && selectedOption?.first != null ||
            (selectedOption?.first == "Other" && otherAccName.isNotBlank() || otherAccName.isNotEmpty())
            && password.isNotBlank()
        ) {
            val account = AccountEntity(
                id = 0,
                accountName = selectedOption?.first!!,
                userName = username,
                email = email,
                mobileNumber = mobileNumber,
            )

            val bytes = password.encodeToByteArray()
            val file = File(context.filesDir, "securify_keys.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            val fos = FileOutputStream(file)

            viewModelScope.launch {
                password =
                    cryptoManager.encrypt(
                        bytes = bytes, outputStream = fos
                    )
                        .decodeToString()
                db.insertAccount(account)
                messages.tryEmit("Password Added!")
                success.value = true
            }
        } else {
            validateFields()
        }
    }
}