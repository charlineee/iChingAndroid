package com.example.ichingandroid.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ichingandroid.R

val ChakraPetch = FontFamily(
    Font(R.font.chakrapetch_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.chakrapetch_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.chakrapetch_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.chakrapetch_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.chakrapetch_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.chakrapetch_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.chakrapetch_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.chakrapetch_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.chakrapetch_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.chakrapetch_bolditalic, FontWeight.Bold, FontStyle.Italic),
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = ChakraPetch,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ChakraPetch,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = ChakraPetch,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)