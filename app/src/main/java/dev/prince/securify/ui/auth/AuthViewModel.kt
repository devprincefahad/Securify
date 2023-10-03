package dev.prince.securify.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.local.EncryptedSharedPrefHelper
import dev.prince.securify.util.AUTH_KEY
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val pref: EncryptedSharedPrefHelper
) : ViewModel() {

    val loginKey = pref.getFromSharedPrefs(AUTH_KEY)

    val isUserLoggedIn = !pref.getFromSharedPrefs(AUTH_KEY).isNullOrEmpty()

    fun saveUserLoginInfo(value: String) {
        pref.saveToSharedPrefs(AUTH_KEY, value)
    }

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

}