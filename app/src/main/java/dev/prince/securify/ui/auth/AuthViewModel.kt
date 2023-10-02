package dev.prince.securify.ui.auth

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

}