package com.example.ichingandroid.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object CastRoute : NavKey

@Serializable
data class ResultRoute(
    val primaryHexNumber: Int,
    val relatingHexNumber: Int?,
    val changingLineIndices: List<Int>,
    val question: String
) : NavKey

