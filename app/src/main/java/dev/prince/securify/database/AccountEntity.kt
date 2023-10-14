package dev.prince.securify.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    var accountName: String,
    var userName : String,
    var email : String,
    var mobileNumber: String
)