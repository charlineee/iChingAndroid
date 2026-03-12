package com.example.ichingandroid.data

import android.content.Context
import kotlinx.serialization.json.Json

open class IChingRepository(private val context: Context?) {

    private val data: Map<String, HexagramData> by lazy {
        val json = context!!.assets.open("iching_wilhelm_translation.json")
            .bufferedReader()
            .use { it.readText() }
        Json { ignoreUnknownKeys = true }
            .decodeFromString<Map<String, HexagramData>>(json)
    }

    open fun getHexagram(number: Int): HexagramData? =
        data[number.toString()]

    open fun getChangingLineTexts(hexNumber: Int, lineNumbers: List<Int>): Map<Int, String> {
        val hex = data[hexNumber.toString()] ?: return emptyMap()
        return lineNumbers.associateWith { line ->
            hex.lines[line.toString()]?.text ?: ""
        }
    }
}