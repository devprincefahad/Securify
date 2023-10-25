package dev.prince.securify.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val accountName: String,
    val userName: String,
    val email: String,
    val mobileNumber: String,
    val password: String,
    val note: String
)