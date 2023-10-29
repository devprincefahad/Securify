package dev.prince.securify.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cardHolderName: String,
    val cardNumber: String,
    val cardExpiryDate: String,
    val cardCvv: String
)