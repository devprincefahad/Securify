package dev.prince.securify.ui.add_card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import dev.prince.securify.util.cardSuggestions
import dev.prince.securify.util.oneShotFlow
import javax.inject.Inject
import kotlin.random.Random

class AddCardViewModel @Inject constructor(

) : ViewModel() {

    val messages = oneShotFlow<String>()

    var cardNumber by mutableStateOf("")

    var cardHolderName by mutableStateOf("")

    var cardExpiryDate by mutableStateOf("")

    var cardCVV by mutableStateOf("")

    var expanded by mutableStateOf(false)

    var cardSuggestionList = SnapshotStateList<String>()

    var expandedProviderField by mutableStateOf(false)
    var selectedOptionText by mutableStateOf("Choose a Provider, eg: Visa")
    var hideKeyboard by mutableStateOf(false)
    var selectedCardImage by mutableIntStateOf(cardSuggestions.first().second)

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


}
