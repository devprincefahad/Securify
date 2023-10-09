package dev.prince.securify.ui.generate

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun GenerateScreen(

) {

    val context = LocalContext.current
    val passwordLength = remember { mutableStateOf(12) }
    val lowerCase = remember { mutableStateOf(true) }
    val upperCase = remember { mutableStateOf(true) }
    val digits = remember { mutableStateOf(true) }
    val specialCharacters = remember { mutableStateOf(true) }
    val password = remember {
        mutableStateOf(
            generatePassword(
                passwordLength.value,
                lowerCase.value,
                upperCase.value,
                digits.value,
                specialCharacters.value
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Password Length: ${passwordLength.value}")
        // Password length slider
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = passwordLength.value.toFloat(),
            onValueChange = { passwordLength.value = it.toInt() },
            valueRange = 8f..15f,
            steps = 7,
        )

        // Checkbox options
        Checkbox(
            checked = lowerCase.value,
            onCheckedChange = { lowerCase.value = it }
        )
        Text("Lower case")
        Checkbox(
            checked = upperCase.value,
            onCheckedChange = { upperCase.value = it }
        )
        Text("Upper case")
        Checkbox(
            checked = digits.value,
            onCheckedChange = { digits.value = it }
        )
        Text("Digits")
        Checkbox(
            checked = specialCharacters.value,
            onCheckedChange = { specialCharacters.value = it }
        )
        Text("Special characters")

        // Generate password button
        Button(
            onClick = {
                password.value = generatePassword(
                    passwordLength.value,
                    lowerCase.value,
                    upperCase.value,
                    digits.value,
                    specialCharacters.value
                )
            }
        ) {
            Text("Generate password")
        }

        // Password text field
        TextField(
            value = password.value,
            onValueChange = {},
            readOnly = true
        )
    }
    BackHandler {
        (context as ComponentActivity).finish()
    }
}

private fun generatePassword(
    length: Int,
    lowerCase: Boolean,
    upperCase: Boolean,
    digits: Boolean,
    specialCharacters: Boolean
): String {

    if (!lowerCase && !upperCase && !digits && !specialCharacters) {
        return "Please select at least one checkbox."
    }

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

@Preview(showBackground = true)
@Composable
fun GenerateScreenPreview() {
    GenerateScreen()
}
