package dev.prince.securify.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.util.Calendar
import java.util.Date
import kotlin.math.absoluteValue
import kotlin.random.Random


class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator() = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val offsetValue = offset.absoluteValue
            if (offsetValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }
}

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