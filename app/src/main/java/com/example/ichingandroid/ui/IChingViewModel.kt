package com.example.ichingandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ichingandroid.model.CoinThrow
import com.example.ichingandroid.model.Hexagram
import com.example.ichingandroid.model.LineType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

class IChingViewModel : ViewModel() {

    private val _throws = MutableStateFlow<List<CoinThrow>>(emptyList())
    val throws: StateFlow<List<CoinThrow>> = _throws.asStateFlow()

    val isComplete: StateFlow<Boolean> = _throws
        .map { it.size >= 6 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val hexagram: StateFlow<Hexagram?> = _throws
        .map { if (it.size == 6) buildHexagram(it) else null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun throwCoins(): CoinThrow {
        val coins = List(3) { if (Random.nextBoolean()) "heads" else "tails" }
        val sum = coins.fold(0) { acc, coin -> acc + if (coin == "heads") 3 else 2 }
        val lineType = when (sum) {
            6    -> LineType.YIN_CHANGING
            7    -> LineType.YANG
            8    -> LineType.YIN
            else -> LineType.YANG_CHANGING
        }
        val coinThrow = CoinThrow(coins, sum, lineType)
        _throws.value = _throws.value + coinThrow
        return coinThrow
    }


    fun reset() {
        _throws.value = emptyList()
    }

    private fun buildHexagram(throws: List<CoinThrow>): Hexagram {
        return HexagramCalculator.buildHexagram(throws)
    }
}
