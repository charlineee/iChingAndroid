package com.example.ichingandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ichingandroid.data.IChingRepository
import com.example.ichingandroid.data.ReadingEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: IChingRepository) : ViewModel() {

    val readings: StateFlow<List<ReadingEntity>> = repository.getAllReadings()
        ?.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        ?: flowOf(emptyList<ReadingEntity>()).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun deleteReading(reading: ReadingEntity) {
        viewModelScope.launch {
            repository.deleteReading(reading)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    companion object {
        fun factory(repository: IChingRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                HistoryViewModel(repository) as T
        }
    }
}