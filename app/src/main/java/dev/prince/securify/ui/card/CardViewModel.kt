package dev.prince.securify.ui.card

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.securify.database.CardDao
import dev.prince.securify.database.CardEntity
import dev.prince.securify.encryption.EncryptionManager
import dev.prince.securify.util.cardSuggestions
import dev.prince.securify.util.oneShotFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val db: CardDao,
    private val encryptionManager: EncryptionManager
) : ViewModel() {

    var isEditScreen by mutableStateOf(false)

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

    fun getCardById(cardId: Int) {
        viewModelScope.launch {
            db.getCardsById(cardId).collect {
                cardNumber = encryptionManager.decrypt(it.cardNumber)
                cardHolderName = encryptionManager.decrypt(it.cardHolderName)
                cardProviderName = it.cardProvider
                cardCVV = encryptionManager.decrypt(it.cardCvv)
                cardExpiryDate = encryptionManager.decrypt(it.cardExpiryDate)
                it.createdAt
            }
        }
    }

    fun validateAndUpdate(id: Int) {
        if (validateFields()) {

            val currentTimeInMillis = System.currentTimeMillis()

            val card = CardEntity(
                id = id,
                cardHolderName = encryptionManager.encrypt(cardHolderName.trim()),
                cardNumber = encryptionManager.encrypt(cardNumber),
                cardExpiryDate = encryptionManager.encrypt(cardExpiryDate),
                cardCvv = encryptionManager.encrypt(cardCVV),
                cardProvider = cardProviderName,
                createdAt = currentTimeInMillis
            )

            viewModelScope.launch {
                db.updateCard(card)
                messages.tryEmit("Credentials Updated!")
                success.value = true
            }

        }
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
        if (!cardCVV.isDigitsOnly()) {
            messages.tryEmit("Invalid Card CVV")
            return false
        }
        if (cardCVV.length < 3) {
            messages.tryEmit("Card cvv must be 3 digits")
            return false
        }
        return true
    }

    fun validateAndInsert() {
        if (validateFields()) {

            val currentTimeInMillis = System.currentTimeMillis()

            val card = CardEntity(
                id = 0,
                cardHolderName = encryptionManager.encrypt(cardHolderName.trim()),
                cardNumber = encryptionManager.encrypt(cardNumber),
                cardExpiryDate = encryptionManager.encrypt(cardExpiryDate),
                cardCvv = encryptionManager.encrypt(cardCVV),
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