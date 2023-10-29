package dev.prince.securify.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AccountEntity::class, CardEntity::class],
    version = 1
)
abstract class SecurifyDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun cardDao(): CardDao
}