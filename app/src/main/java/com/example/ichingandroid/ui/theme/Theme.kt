package com.example.ichingandroid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

val LocalIsDarkTheme = compositionLocalOf { true }

private val DarkColorScheme = darkColorScheme(
    background = IChingBackground,
    surface = IChingBackground,
    onBackground = HexagramSubtext,
    onSurface = HexagramSubtext,
    primary = ChangingLineTint,
    secondary = IChingGold
)

private val LightColorScheme = lightColorScheme(
    background = LineTint,
    surface = LineTint,
    onBackground = IChingBackground,
    onSurface = IChingBackground,
    primary = ChangingLineTint,
    secondary = IChingGold
)

@Composable
fun IchingandroidTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}