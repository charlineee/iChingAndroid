package com.example.ichingandroid.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class DatabaseConverters {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toList(data: String): List<Int> {
        return Json.decodeFromString(data)
    }
}