package dev.prince.securify.ui.add_card

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.CardDao
import dev.prince.securify.database.CardEntity
import dev.prince.securify.util.cardSuggestions
import dev.prince.securify.util.oneShotFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val db: CardDao
) : ViewModel() {

    val messages = oneShotFlow<String>()

    var cardNumber by mutableStateOf("")

    var cardHolderName by mutableStateOf("")

    var cardExpiryDate by mutableStateOf("")

    var cardCVV by mutableStateOf("")

    var expanded by mutableStateOf(false)

    var cardProviderName by mutableStateOf("Select Card Provider")

    var expandedProviderField by mutableStateOf(false)
    var hideKeyboard by mutableStateOf(false)
    var selectedCardImage by mutableIntStateOf(cardSuggestions.first().second)

    val success = mutableStateOf(false)

    // Making XXXX-XXXX-XXXX-XXXX string.
    val visualTransformation = object : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
            var out = ""

            for (i in trimmed.indices) {
                out += trimmed[i]
                if (i % 4 == 3 && i != 15) out += " "
            }
            return TransformedText(
                AnnotatedString(out),
                creditCardOffsetMapping
            )
        }
    }

    val creditCardOffsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 7) return offset + 1
            if (offset <= 11) return offset + 2
            if (offset <= 16) return offset + 3
            return 19
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 9) return offset - 1
            if (offset <= 14) return offset - 2
            if (offset <= 19) return offset - 3
            return 16
        }
    }

    fun formatCardNumber(cardNumber: String): String {
        val trimmed = if (cardNumber.length >= 16) cardNumber.substring(0..15) else cardNumber
        var out = ""

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += " "
        }

        return out
    }

    fun formatExpiryDate(expiryDate: String): String {
        val trimmed = if (expiryDate.length >= 4) expiryDate.substring(0..3) else expiryDate
        var out = ""

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1) out += "/" // Add a slash after the first two characters
        }

        return out
    }

    private fun validateFields(): Boolean {
        if (cardProviderName == "Select Card Provider") {
            messages.tryEmit("Please choose a Card Provider")
            return false
        }
        if (cardNumber.isEmpty()) {
            messages.tryEmit("Please provide Card Number")
            return false
        }
        if (cardNumber.length < 16) {
            messages.tryEmit("Card number must be 16 digits long")
            return false
        }
        if (!cardNumber.isDigitsOnly()) {
            messages.tryEmit("Invalid Card Number")
            return false
        }
        if (cardHolderName.isEmpty()) {
            messages.tryEmit("Please provide Card holder's name")
            return false
        }
        if (cardExpiryDate.isEmpty()) {
            messages.tryEmit("Please provide Card Expiry Date")
            return false
        }
        if (!validateExpiryDate(cardExpiryDate)) {
            return false
        }
        if (cardCVV.isEmpty()) {
            messages.tryEmit("Please provide Card CVV")
            return false
        }
        if (cardNumber.length < 3) {
            messages.tryEmit("Card cvv must be 3 digits")
            return false
        }
        return true
    }

    fun validateAndInsert() {
        if (validateFields()) {

            val currentTimeInMillis = System.currentTimeMillis()
            val formattedExpiryDate = formatExpiryDate(cardExpiryDate)

            val card = CardEntity(
                id = 0,
                cardHolderName = cardHolderName.trim(),
                cardNumber = cardNumber,
                cardExpiryDate = formattedExpiryDate,
                cardCvv = cardCVV,
                cardProvider = cardProviderName,
                createdAt = currentTimeInMillis
            )

            viewModelScope.launch {
                db.insertCard(card)
                messages.tryEmit("Credentials Added!")
                success.value = true
            }

        }
    }

    private val gradientOptions = listOf(
        listOf(Color(0xFF6C72CB), Color(0xFF0078FF)), // Blue Gradient
        listOf(Color(0xFF8A2387), Color(0xFFE94057)), // Pink Gradient
        listOf(Color(0xFF56CCF2), Color(0xFF2F80ED)), // Sky Blue Gradient
        listOf(Color(0xFFFFD23F), Color(0xFFFF6B6B)), // Sunset Gradient
        listOf(Color(0xFF6A3093), Color(0xFFA044FF)), // Purple Gradient
        listOf(Color(0xFFF7B733), Color(0xFFFC4A1A)), // Orange Gradient
        listOf(Color(0xFF00C9FF), Color(0xFF92FE9D)), // Turquoise Gradient
        listOf(Color(0xFF00F260), Color(0xFF0575E6)), // Green Gradient
        listOf(Color(0xFF693B52), Color(0xFF1B1B1E)), // Dark Red Gradient
        listOf(Color(0xFF00B4DB), Color(0xFF0083B0))  // Ocean Blue Gradient
    )

    private val randomIndex = Random.nextInt(gradientOptions.size)
    val randomGradient = gradientOptions[randomIndex]

    private fun validateExpiryDate(cardExpiryDate: String): Boolean {

        val currentDate = Date()
        val calender = Calendar.getInstance()

        val userMonth = cardExpiryDate.substring(0, 2).toInt()
        val userYear = cardExpiryDate.substring(2, 4).toInt()
        val currentYear = calender.get(Calendar.YEAR) % 100

        if (userMonth < 1 || userMonth > 12) {
            messages.tryEmit("Invalid month. Please enter a valid month between 01 and 12.")
            return false
        }

        if (userYear < currentYear) {
            messages.tryEmit("Expiry year is in the past. Please enter a valid future year.")
            return false
        }

        if (userYear == currentYear && userMonth < currentDate.month + 1) {
            messages.tryEmit("Expiry date is in the past. Please enter a valid future date.")
            return false
        }

        return true
    }

}
