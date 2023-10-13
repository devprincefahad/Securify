package dev.prince.securify.ui.generate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun showUncheckedToggleMsg(){
        messages.tryEmit("Please toggle at least one option.")
    }

    fun generatePassword(
        length: Int,
        lowerCase: Boolean,
        upperCase: Boolean,
        digits: Boolean,
        specialCharacters: Boolean
    ): String {

        val chars = mutableListOf<Char>()
        val symbols = "!@#$%&*+=-~?/_"
        if (lowerCase) chars.addAll('a'..'z')
        if (upperCase) chars.addAll('A'..'Z')
        if (digits) chars.addAll('0'..'9')
        if (specialCharacters) chars.addAll(symbols.toList())

        val password = StringBuilder()
        for (i in 0 until length) {
            password.append(chars.random())
        }

        return password.toString()

    }


}