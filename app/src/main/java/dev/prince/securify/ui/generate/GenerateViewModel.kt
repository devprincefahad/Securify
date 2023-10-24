package dev.prince.securify.ui.generate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.util.generatePassword
import dev.prince.securify.util.oneShotFlow
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor() : ViewModel() {

    val messages = oneShotFlow<String>()

    var passwordLength by mutableStateOf(12)
    var lowerCase by mutableStateOf(true)
    var upperCase by mutableStateOf(true)
    var digits by mutableStateOf(true)
    var specialCharacters by mutableStateOf(true)


    var password by mutableStateOf(
        generatePassword(
            passwordLength,
            lowerCase,
            upperCase,
            digits,
            specialCharacters
        )
    )

    fun showCopyMsg() {
        messages.tryEmit("Password copied to clipboard.")
    }

    fun checkToggleAndSave() {
        if (!(lowerCase || upperCase || digits || specialCharacters)) {
            messages.tryEmit("Please toggle at least one option.")
        } else {
            password = generatePassword(
                passwordLength,
                lowerCase,
                upperCase,
                digits,
                specialCharacters
            )
        }
    }

}