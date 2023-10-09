package dev.prince.securify.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.local.EncryptedSharedPrefHelper
import dev.prince.securify.util.AUTH_KEY
import dev.prince.securify.util.oneShotFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val prefs: EncryptedSharedPrefHelper
) : ViewModel() {

    // For SharedPrefs
    private val loginKey = prefs.getFromSharedPrefs(AUTH_KEY)

    val isUserLoggedIn = !prefs.getFromSharedPrefs(AUTH_KEY).isNullOrEmpty()

    private fun saveUserLoginInfo(value: String) {
        prefs.saveToSharedPrefs(AUTH_KEY, value)
    }

    // For Setup Key Screen
    var key by mutableStateOf("")
    var keyVisible by mutableStateOf(false)
    var isErrorForKey by mutableStateOf(false)

    var confirmKey by mutableStateOf("")
    var confirmKeyVisible by mutableStateOf(false)
    var isErrorForConfirmKey by mutableStateOf(false)

    val maxLength = 6

    fun validateKey(key: String) {
        isErrorForKey = key.length > maxLength
    }

    fun validateConfirmKey(confirmKey: String) {
        isErrorForConfirmKey = confirmKey.length > maxLength
    }

    var isLoading by mutableStateOf(false)

    val messages = oneShotFlow<String>()

    val navigateToHome = oneShotFlow<Unit>()

    fun saveMasterKeyValidation() {

        if (key.isEmpty() and confirmKey.isEmpty()) {
            messages.tryEmit("Please enter a Master Key")
        }
        if (key != confirmKey) {
            messages.tryEmit("Please enter correct Master Key")
        }
        if (key.length and confirmKey.length > maxLength) {
            messages.tryEmit("A Master Key can only have 6 characters")
        }

        if (key.isNotEmpty() and confirmKey.isNotEmpty()) {
            if (key == confirmKey) {
                saveUserLoginInfo(confirmKey)
                viewModelScope.launch {
                    isLoading = true
                    delay(2000)
                    isLoading = false
                    navigateToHome.tryEmit(Unit)
                }
            }
        }
    }

    //For Unlock Screen
    var unlockKey by mutableStateOf("")
    var unlockKeyVisible by mutableStateOf(false)
    var isErrorForUnlock by (mutableStateOf(false))
    var isLoadingForUnlock by (mutableStateOf(false))

    fun validateUnlockKey(unlockKey: String) {
        isErrorForUnlock = unlockKey.length > maxLength
    }

    fun proceedValidation() {
        if (unlockKey.isEmpty()) {
            messages.tryEmit("Please enter a Master Key")
        } else if (unlockKey == loginKey) {
            viewModelScope.launch {
                isLoadingForUnlock = true
                delay(2000)
                isLoadingForUnlock = false
                navigateToHome.tryEmit(Unit)
            }
        } else {
            messages.tryEmit(
                "Incorrect Master Key," +
                        " Please check again"
            )
        }
    }
}