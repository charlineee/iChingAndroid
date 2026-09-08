package com.example.ichingandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ichingandroid.domain.HexagramCalculator
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

    private val _coinThrows = MutableStateFlow<List<CoinThrow>>(emptyList())
    val coinThrows: StateFlow<List<CoinThrow>> = _coinThrows.asStateFlow()

    val isComplete: StateFlow<Boolean> = _coinThrows
        .map { it.size == UIConstants.HEXAGRAM_LINE_COUNT }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val hexagram: StateFlow<Hexagram?> = _coinThrows
        .map { if (it.size == UIConstants.HEXAGRAM_LINE_COUNT) buildHexagram(it) else null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun throwCoins(): CoinThrow {
        val coins = List(UIConstants.COIN_COUNT) { if (Random.nextBoolean()) "heads" else "tails" }
        val sum = coins.fold(0) {
            acc, coin -> acc + if (coin == "heads") IChingLogic.COIN_HEADS_VALUE else IChingLogic.COIN_TAILS_VALUE }
        val lineType = when (sum) {
            IChingLogic.SUM_YIN_CHANGING    -> LineType.YIN_CHANGING
            IChingLogic.SUM_YANG            -> LineType.YANG
            IChingLogic.SUM_YIN             -> LineType.YIN
            else                            -> LineType.YANG_CHANGING
        }
        val coinThrow = CoinThrow(coins, sum, lineType)
        
        if (_coinThrows.value.size < UIConstants.HEXAGRAM_LINE_COUNT) {
            _coinThrows.value += coinThrow
        }
        
        return coinThrow
    }


    fun reset() {
        _coinThrows.value = emptyList()
    }

    private fun buildHexagram(coinThrows: List<CoinThrow>): Hexagram {
        return HexagramCalculator.buildHexagram(coinThrows)
    }
}
