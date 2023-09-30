package dev.prince.securify.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("DELETE FROM `account`")
    suspend fun deleteAllAccounts()

    @Query("SELECT * FROM account ORDER BY id ASC")
    fun getAllAccounts(): List<Account>

    @Query("SELECT * FROM `account` WHERE id = :id")
    suspend fun getAccountById(id: Int): Account?

}