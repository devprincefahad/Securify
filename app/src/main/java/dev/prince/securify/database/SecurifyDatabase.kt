package dev.prince.securify.database

import androidx.room.RoomDatabase

abstract class SecurifyDatabase  : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}