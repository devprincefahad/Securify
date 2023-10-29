package dev.prince.securify.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(cardEntity: CardEntity)

    @Update
    suspend fun updateCard(cardEntity: CardEntity)

    @Delete
    suspend fun deleteCard(cardEntity: CardEntity)

    @Query("SELECT * FROM card ORDER BY id ASC")
    fun getAllCard(): Flow<List<CardEntity>>

    @Query("SELECT * FROM `card` WHERE id = :id")
    fun getCardById(id: Int): Flow<CardEntity>

}