package com.example.ichingandroid.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HexagramData(
    val hex: Int,
    @SerialName("hex_font") val hexFont: String,
    @SerialName("trad_chinese") val tradChinese: String,
    val pinyin: String,
    val english: String,
    @SerialName("wilhelm_judgment") val judgment: WilhelmText,
    @SerialName("wilhelm_image") val image: WilhelmText,
    @SerialName("wilhelm_lines") val lines: Map<String, WilhelmLine>
)

@Serializable
data class WilhelmText(val text: String)

@Serializable
data class WilhelmLine(val text: String)