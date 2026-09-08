package com.example.ichingandroid.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.ichingandroid.data.IChingRepository
import com.example.ichingandroid.data.provideDatabaseBuilder
import com.example.ichingandroid.model.Hexagram
import kotlinx.coroutines.launch

@Composable
fun IChingNavigation(
    onToggleTheme: () -> Unit
) {
    val backStack = rememberNavBackStack(CastRoute)
    val context = LocalContext.current
    val repository = remember {
        val database = provideDatabaseBuilder().build()
        val jsonString = context.assets.open("iching_wilhelm_translation.json")
            .bufferedReader().use { it.readText() }
        IChingRepository(database.readingDao(), jsonString)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.factory(repository)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HistoryDrawerContent(
                viewModel = historyViewModel,
                onReadingClick = { reading ->
                    scope.launch { drawerState.close() }
                    backStack.add(
                        ResultRoute(
                            primaryHexNumber = reading.primaryHexNumber,
                            relatingHexNumber = reading.relatingHexNumber,
                            changingLineIndices = reading.changingLineNumbers,
                            question = reading.question
                        )
                    )
                }
            )
        }
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
        ) { key ->
            when (key) {
                is CastRoute -> NavEntry(key) {
                    val onOpenHistory = { scope.launch { drawerState.open() }; Unit }
                    val onNavigateToResult = { hexagram: Hexagram, question: String ->
                        val changingIndices = hexagram.lines
                            .mapIndexedNotNull { i, line -> if (line.isChanging()) i + 1 else null }
                        backStack.add(
                            ResultRoute(
                                primaryHexNumber = hexagram.primaryNumber,
                                relatingHexNumber = hexagram.relatingNumber,
                                changingLineIndices = changingIndices,
                                question = question
                            )
                        )
                        Unit
                    }
                    CastScreen(
                        actions = CastScreenActions(
                            onToggleTheme = onToggleTheme,
                            onOpenHistory = onOpenHistory,
                            onNavigateToResult = onNavigateToResult
                        )
                    )
                }
                is ResultRoute -> NavEntry(key) {
                    val interpViewModel: HexagramResultViewModel = viewModel(
                        factory = HexagramResultViewModel.factory(repository)
                    )
                    
                    val isSaved by interpViewModel.isSaved.collectAsState()

                    LaunchedEffect(key) {
                        interpViewModel.checkSavedStatus(
                            question = key.question,
                            primaryHexNumber = key.primaryHexNumber,
                            relatingHexNumber = key.relatingHexNumber,
                            changingLineNumbers = key.changingLineIndices
                        )
                    }

                    val reading = interpViewModel.buildReading(
                        primaryHexNumber = key.primaryHexNumber,
                        relatingHexNumber = key.relatingHexNumber,
                        changingLineNumbers = key.changingLineIndices
                    )
                    reading?.let {
                        HexagramResult(
                            reading = it,
                            isSaved = isSaved,
                            onBack = { backStack.removeLastOrNull() },
                            onSave = {
                                interpViewModel.saveReading(
                                    question = key.question,
                                    primaryHexNumber = key.primaryHexNumber,
                                    relatingHexNumber = key.relatingHexNumber,
                                    changingLineNumbers = key.changingLineIndices
                                )
                            }
                        )
                    }
                }
                else -> NavEntry(key) {}
            }
        }
    }
}