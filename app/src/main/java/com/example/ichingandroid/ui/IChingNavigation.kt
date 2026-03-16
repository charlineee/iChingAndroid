package com.example.ichingandroid.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.ichingandroid.data.IChingRepository

@Composable
fun IChingNavigation(
    onToggleTheme: () -> Unit
) {
    val backStack = rememberNavBackStack(CastRoute)
    val context = LocalContext.current
    val repository = remember { IChingRepository(context) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
    ) { key ->
        when (key) {
            is CastRoute -> NavEntry(key) {
                CastScreen(
                    onToggleTheme = onToggleTheme,
                    onNavigateToResult = { hexagram ->
                        val changingIndices = hexagram.lines
                            .mapIndexedNotNull { i, line -> if (line.isChanging()) i + 1 else null }
                        backStack.add(
                            ResultRoute(
                                primaryHexNumber = hexagram.primaryNumber,
                                relatingHexNumber = hexagram.relatingNumber,
                                changingLineIndices = changingIndices
                            )
                        )
                    }
                )
            }
            is ResultRoute -> NavEntry(key) {
                val interpViewModel: HexagramResultViewModel = viewModel(
                    factory = HexagramResultViewModel.factory(repository)
                )
                val reading = interpViewModel.buildReading(
                    primaryHexNumber = key.primaryHexNumber,
                    relatingHexNumber = key.relatingHexNumber,
                    changingLineNumbers = key.changingLineIndices
                )
                reading?.let {
                    HexagramResult(
                        reading = it,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
            else -> NavEntry(key) {}
        }
    }
}