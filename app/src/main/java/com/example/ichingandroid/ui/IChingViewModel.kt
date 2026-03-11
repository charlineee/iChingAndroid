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
        val lines = throws.map { it.lineType }
        val primaryBinary = lines.map { it.isYang() }

        val relatingBinary = lines.map {
            when (it) {
                LineType.YANG_CHANGING -> false
                LineType.YIN_CHANGING  -> true
                else                   -> it.isYang()
            }
        }

        val primaryNumber  = lookupHexagram(primaryBinary)
        val relatingNumber = lookupHexagram(relatingBinary)
        val hasChanging    = lines.any { it.isChanging() }

        return Hexagram(
            lines            = lines,
            primaryNumber    = primaryNumber,
            primaryName      = HEXAGRAM_NAMES[primaryNumber] ?: "Unknown",
            relatingNumber   = if (hasChanging) relatingNumber else null,
            relatingName     = if (hasChanging) HEXAGRAM_NAMES[relatingNumber] ?: "Unknown" else null,
            hasChangingLines = hasChanging
        )
    }

    companion object {

        private fun lookupHexagram(lines: List<Boolean>): Int {
            val lower = trigramIndex(lines[0], lines[1], lines[2])
            val upper = trigramIndex(lines[3], lines[4], lines[5])
            return HEXAGRAM_TABLE[upper][lower]
        }

        private fun trigramIndex(l1: Boolean, l2: Boolean, l3: Boolean): Int {
            return when {
                // trigram logic:
                !l1 && !l2 && !l3 -> 0  // ☷ Kun   — Earth
                l1  && !l2 && !l3 -> 1  // ☳ Zhen  — Thunder
                !l1 && l2  && !l3 -> 2  // ☵ Kan   — Water
                l1  && l2  && !l3 -> 3  // ☱ Dui   — Lake
                !l1 && !l2 && l3  -> 4  // ☶ Gen   — Mountain
                l1  && !l2 && l3  -> 5  // ☴ Xun   — Wind
                !l1 && l2  && l3  -> 6  // ☲ Li    — Fire
                else              -> 7  // ☰ Qian  — Heaven
            }
        }

        private val HEXAGRAM_TABLE = arrayOf(
            intArrayOf(2,  24,  7,  19,  15,  46,  36, 11),
            intArrayOf(16,  51, 40,  54,  62,  32,  55, 34),
            intArrayOf(8,   3, 29,  60,  39,  48,  63, 5 ),
            intArrayOf(45, 17, 47,  58,  31,  28,  49, 43),
            intArrayOf(23, 27, 4,   41,  52,  18,  22, 26),
            intArrayOf(20, 42, 59,  61,  53,  57,  37, 9 ),
            intArrayOf(35, 21, 64,  38,  56,  50,  30, 14),
            intArrayOf(12, 25, 6,   10,  33,  44,  13, 1 ),
        )

        // hexagram names as placeholder for actual explainations
        val HEXAGRAM_NAMES = mapOf(
            1  to "The Creative",        2  to "The Receptive",
            3  to "Difficult Beginnings",4  to "Youthful Folly",
            5  to "Waiting",             6  to "Conflict",
            7  to "The Army",            8  to "Holding Together",
            9  to "Small Taming",        10 to "Treading",
            11 to "Peace",               12 to "Standstill",
            13 to "Fellowship",          14 to "Great Possession",
            15 to "Modesty",             16 to "Enthusiasm",
            17 to "Following",           18 to "Work on the Decayed",
            19 to "Approach",            20 to "Contemplation",
            21 to "Biting Through",      22 to "Grace",
            23 to "Splitting Apart",     24 to "Return",
            25 to "Innocence",           26 to "Great Taming",
            27 to "Nourishment",         28 to "Great Excess",
            29 to "The Abysmal",         30 to "The Clinging",
            31 to "Influence",           32 to "Duration",
            33 to "Retreat",             34 to "Great Power",
            35 to "Progress",            36 to "Darkening of the Light",
            37 to "The Family",          38 to "Opposition",
            39 to "Obstruction",         40 to "Deliverance",
            41 to "Decrease",            42 to "Increase",
            43 to "Breakthrough",        44 to "Coming to Meet",
            45 to "Gathering Together",  46 to "Pushing Upward",
            47 to "Oppression",          48 to "The Well",
            49 to "Revolution",          50 to "The Cauldron",
            51 to "The Arousing",        52 to "Keeping Still",
            53 to "Development",         54 to "The Marrying Maiden",
            55 to "Abundance",           56 to "The Wanderer",
            57 to "The Gentle",          58 to "The Joyous",
            59 to "Dispersion",          60 to "Limitation",
            61 to "Inner Truth",         62 to "Small Excess",
            63 to "After Completion",    64 to "Before Completion"
        )
    }
}
