package com.example.ichingandroid.model

data class Hexagram(
    val lines: List<LineType>,
    val primaryNumber: Int,
    val primaryName: String,
    val relatingNumber: Int?,
    val relatingName: String?,
    val hasChangingLines: Boolean
)