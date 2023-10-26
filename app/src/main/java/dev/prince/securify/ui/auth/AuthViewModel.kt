package dev.prince.securify.ui.auth

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.prince.securify.local.SharedPrefHelper
import dev.prince.securify.util.oneShotFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val prefs: SharedPrefHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // For SharedPrefs
    private val loginKey = prefs.masterKey

    val isUserLoggedIn = prefs.masterKey.isNotEmpty()

    private fun saveUserLoginInfo(value: String) {
        prefs.masterKey = value
    }

    // For Setup Key Screen
    var key by mutableStateOf("")
    var keyVisible by mutableStateOf(false)

    var confirmKey by mutableStateOf("")
    var confirmKeyVisible by mutableStateOf(false)

    var isLoading by mutableStateOf(false)

    val messages = oneShotFlow<String>()

    val navigateToHome = oneShotFlow<Unit>()

    fun validateAndSaveMasterKey() {

        if (key.isEmpty() and confirmKey.isEmpty()) {
            messages.tryEmit("Please enter a Master Key")
            return
        }
        if (key != confirmKey) {
            messages.tryEmit("Please enter correct Master Key")
            return
        }
        if (key.length < 6) {
            messages.tryEmit("A Master Key should at least have 6 characters")
            return
        }

        val isNotEmpty = key.isNotEmpty() and confirmKey.isNotEmpty()
        val isKeySame = key == confirmKey

        if (isNotEmpty and isKeySame) {
            saveUserLoginInfo(confirmKey)
            viewModelScope.launch {
                isLoading = true
                delay(2000)
                isLoading = false
                navigateToHome.tryEmit(Unit)
            }
        }
    }

    //For Unlock Screen
    var unlockKey by mutableStateOf("")
    var unlockKeyVisible by mutableStateOf(false)
    var isLoadingForUnlock by (mutableStateOf(false))

    fun validateAndOpen() {
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


    //For Update key
    var oldKey by mutableStateOf("")
    var oldKeyVisible by mutableStateOf(false)

    var newKey by mutableStateOf("")
    var newKeyVisible by mutableStateOf(false)

    var confirmNewKey by mutableStateOf("")
    var confirmNewKeyVisible by mutableStateOf(false)


    fun validateAndUpdateMasterKey() {

        if (oldKey.isEmpty()) {
            messages.tryEmit("Please enter Old Key")
            return
        }
        if (newKey.isEmpty()) {
            messages.tryEmit("Please enter New Master Key")
            return
        }
        if (confirmNewKey.isEmpty()) {
            messages.tryEmit("Please enter Confirm Master Key")
            return
        }
        if (newKey != confirmNewKey) {
            messages.tryEmit("Please enter correct Master Key")
            return
        }
        if (oldKey != loginKey) {
            messages.tryEmit("Please enter correct old key")
            return
        }
        if (oldKey == newKey) {
            messages.tryEmit("New Key and Old Key cannot be the same")
            return
        }

        val isNotEmpty = oldKey.isNotEmpty() and newKey.isNotEmpty() and confirmNewKey.isNotEmpty()
        val isOldKeySame = oldKey == loginKey
        val isKeyNotSame = oldKey != newKey

        if (isNotEmpty and isOldKeySame and isKeyNotSame) {
            saveUserLoginInfo(newKey)
            viewModelScope.launch {
                isLoading = true
                delay(2000)
                isLoading = false
                navigateToHome.tryEmit(Unit)
            }
        }
    }

    // For Biometric

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setAllowedAuthenticators(BIOMETRIC_STRONG)
        .setTitle("Fingerprint Unlock")
        .setSubtitle("Use your fingerprint to unlock Securify")
        .setNegativeButtonText("Cancel")
        .setDeviceCredentialAllowed(true)
        .build()

    fun showSnackBarMsg(msg: String) {
        messages.tryEmit(msg)
    }

    val getState: Boolean
        get() = prefs.getSwitchState()
}