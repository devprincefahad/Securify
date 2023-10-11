package dev.prince.securify.ui.add

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.util.oneShotFlow
import javax.inject.Inject

@HiltViewModel
class AddPasswordViewModel @Inject constructor(

) : ViewModel() {

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

    fun validateFields() {
        if (username.isEmpty() && email.isBlank() && mobileNumber.isBlank()) {
            messages.tryEmit("Please provide a username, email, or mobile number.")
        }
        /*if (password.length < 6) {
            messages.tryEmit("Password length must be 6 characters or more")
        }*/
        if(selectedOption?.first == null){
            messages.tryEmit("Please Choose an account.")
        }
        if (selectedOption?.first == "Other" && otherAccName.isBlank()){
            messages.tryEmit("Please provide other account name")
        }
        if (password.isBlank()) {
            messages.tryEmit("Password cannot be empty")
        }
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            messages.tryEmit("Invalid email address")
        }
    }


}