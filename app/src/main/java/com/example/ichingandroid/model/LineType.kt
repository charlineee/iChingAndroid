package com.example.ichingandroid.model

enum class LineType {
    // iching logic:
    YANG,           // 7  — solid line
    YIN,            // 8  — broken line
    YANG_CHANGING,  // 9  — solid, becomes yin
    YIN_CHANGING;   // 6  — broken, becomes yang

    fun isYang() = this == YANG || this == YANG_CHANGING
    fun isChanging() = this == YANG_CHANGING || this == YIN_CHANGING
}