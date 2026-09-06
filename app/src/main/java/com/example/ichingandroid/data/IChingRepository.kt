package com.example.ichingandroid.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

open class IChingRepository(
    private val context: Context,
    private val readingDao: ReadingDao? = null
) {

    private val json = Json { 
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val data: Map<String, HexagramData> by lazy {
        try {
            context.assets.open("iching_wilhelm_translation.json")
                .bufferedReader()
                .use { it.readText() }
                .let { json.decodeFromString<Map<String, HexagramData>>(it) }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    open fun getHexagram(number: Int): HexagramData? =
        data[number.toString()]

    open fun getChangingLineTexts(hexNumber: Int, lineNumbers: List<Int>): Map<Int, String> {
        val hex = data[hexNumber.toString()] ?: return emptyMap()
        return lineNumbers.associateWith { line ->
            hex.lines[line.toString()]?.text ?: ""
        }
    }

    // Database operations
    open fun getAllReadings(): Flow<List<ReadingEntity>>? = readingDao?.getAllReadings()

    open suspend fun saveReading(reading: ReadingEntity) {
        readingDao?.insertReading(reading)
    }

    open suspend fun findReading(
        question: String,
        primaryHex: Int,
        relatingHex: Int?,
        changingLines: List<Int>
    ): ReadingEntity? = readingDao?.findReading(question, primaryHex, relatingHex, changingLines)

    open suspend fun deleteReading(reading: ReadingEntity) {
        readingDao?.deleteReading(reading)
    }

    open suspend fun clearHistory() {
        readingDao?.clearHistory()
    }
}