package com.example.ichingandroid.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingDao {
    @Query("SELECT * FROM readings ORDER BY timestamp DESC")
    fun getAllReadings(): Flow<List<ReadingEntity>>

    @Query("SELECT * FROM readings ORDER BY timestamp DESC")
    suspend fun getHistory(): List<ReadingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: ReadingEntity)

    @Query("SELECT * FROM readings WHERE question = :question AND primaryHexNumber = :primaryHex AND relatingHexNumber = :relatingHex AND changingLineNumbers = :changingLines LIMIT 1")
    suspend fun findReading(
        question: String,
        primaryHex: Int,
        relatingHex: Int?,
        changingLines: List<Int>
    ): ReadingEntity?

    @Delete
    suspend fun deleteReading(reading: ReadingEntity)

    @Query("DELETE FROM readings")
    suspend fun clearHistory()
}
