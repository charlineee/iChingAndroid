package com.example.ichingandroid.model

data class CoinThrow(
    val coins: List<String>,
    val sum: Int,
    val lineType: LineType,
    val drawableVariant: Int = (0..1).random()
)