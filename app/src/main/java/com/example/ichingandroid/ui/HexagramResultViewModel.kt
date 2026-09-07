package com.example.ichingandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ichingandroid.data.HexagramData
import com.example.ichingandroid.data.IChingRepository
import com.example.ichingandroid.data.ReadingEntity
import com.example.ichingandroid.domain.HexagramCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HexagramResultViewModel(private val repository: IChingRepository) : ViewModel() {

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

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
            primaryName = HexagramCalculator.HEXAGRAM_NAMES[primaryHexNumber] ?: primary.english,
            relatingHex = relating,
            relatingName = relating?.let { HexagramCalculator.HEXAGRAM_NAMES[it.hex] ?: it.english },
            changingLines = changingLines,
            judgment = primary.judgment.text,
            image = primary.image.text
        )
    }

    fun saveReading(
        question: String,
        primaryHexNumber: Int,
        relatingHexNumber: Int?,
        changingLineNumbers: List<Int>
    ) {
        viewModelScope.launch {
            val existing = repository.findReading(
                question = question,
                primaryHex = primaryHexNumber,
                relatingHex = relatingHexNumber,
                changingLines = changingLineNumbers
            )
            if (existing == null) {
                repository.saveReading(
                    ReadingEntity(
                        question = question,
                        timestamp = System.currentTimeMillis(),
                        primaryHexNumber = primaryHexNumber,
                        relatingHexNumber = relatingHexNumber,
                        changingLineNumbers = changingLineNumbers
                    )
                )
                _isSaved.value = true
            }
        }
    }

    fun checkSavedStatus(
        question: String,
        primaryHexNumber: Int,
        relatingHexNumber: Int?,
        changingLineNumbers: List<Int>
    ) {
        viewModelScope.launch {
            val existing = repository.findReading(
                question = question,
                primaryHex = primaryHexNumber,
                relatingHex = relatingHexNumber,
                changingLines = changingLineNumbers
            )
            _isSaved.value = existing != null
        }
    }

    companion object Companion {
        fun factory(repository: IChingRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                HexagramResultViewModel(repository) as T
        }
    }
}