package com.example.ichingandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ichingandroid.data.HexagramData
import com.example.ichingandroid.data.IChingRepository

class HexagramResultViewModel(private val repository: IChingRepository) : ViewModel() {

    data class ReadingResult(
        val primaryHex: HexagramData,
        val primaryName: String,
        val relatingHex: HexagramData?,       // null if no changing lines
        val relatingName: String?,            // null if no changing lines
        val changingLines: Map<Int, String>,   // line number → text
        val judgment: String,
        val image: String
    )

    fun buildReading(
        primaryHexNumber: Int,
        relatingHexNumber: Int?,
        changingLineNumbers: List<Int>
    ): ReadingResult? {
        val primary = repository.getHexagram(primaryHexNumber) ?: return null
        val relating = relatingHexNumber?.let { repository.getHexagram(it) }
        val changingLines = repository.getChangingLineTexts(primaryHexNumber, changingLineNumbers)

        return ReadingResult(
            primaryHex = primary,
            primaryName = IChingViewModel.HEXAGRAM_NAMES[primaryHexNumber] ?: primary.english,
            relatingHex = relating,
            relatingName = relating?.let { IChingViewModel.HEXAGRAM_NAMES[it.hex] ?: it.english },
            changingLines = changingLines,
            judgment = primary.judgment.text,
            image = primary.image.text
        )
    }

    companion object Companion {
        fun factory(repository: IChingRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                HexagramResultViewModel(repository) as T
        }
    }
}