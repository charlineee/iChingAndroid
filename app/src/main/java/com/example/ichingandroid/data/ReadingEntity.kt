package com.example.ichingandroid.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "readings")
data class ReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val timestamp: Long,
    val primaryHexNumber: Int,
    val relatingHexNumber: Int?,
    val changingLineNumbers: List<Int>
)