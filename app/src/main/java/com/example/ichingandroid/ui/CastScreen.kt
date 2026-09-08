package com.example.ichingandroid.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ichingandroid.R
import com.example.ichingandroid.R.string.book_of_changes
import com.example.ichingandroid.R.string.cast_again
import com.example.ichingandroid.R.string.iching_chinese
import com.example.ichingandroid.R.string.throw_coins
import com.example.ichingandroid.model.Hexagram
import com.example.ichingandroid.ui.theme.ChakraPetch
import com.example.ichingandroid.ui.theme.IchingandroidTheme
import com.example.ichingandroid.ui.theme.LocalIsDarkTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

data class CastScreenActions(
    val onToggleTheme: () -> Unit = {},
    val onOpenHistory: () -> Unit = {},
    val onNavigateToResult: (Hexagram, String) -> Unit = { _, _ -> }
)

@Composable
fun CastScreen(
    modifier: Modifier = Modifier,
    viewModel: IChingViewModel = viewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isDarkTheme: Boolean = LocalIsDarkTheme.current,
    actions: CastScreenActions = CastScreenActions()
) {
    var isFlipping by remember { mutableStateOf(false) }
    var coinFaces by remember { mutableStateOf(listOf("heads", "heads", "heads")) }
    val coinThrows by viewModel.coinThrows.collectAsStateWithLifecycle()
    val hexagram by viewModel.hexagram.collectAsStateWithLifecycle()
    val isComplete by viewModel.isComplete.collectAsStateWithLifecycle()
    val inputState = rememberTextFieldState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        VignetteOverlay()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .imePadding()
        ) {
            HeaderSection()

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier.height(200.dp).fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                HexagramLineDisplay(coinThrows = coinThrows, isComplete = isComplete)
            }

            Spacer(modifier = Modifier.height(24.dp))

            CoinsSection(isFlipping, coinFaces, isDarkTheme)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                state = inputState,
                placeholder = { Text(stringResource(R.string.question_input_placeholder)) }
            )

            ActionButtons(
                isComplete = isComplete,
                isFlipping = isFlipping,
                coinThrowsCount = coinThrows.size,
                onReset = {
                    viewModel.reset()
                    coinFaces = listOf("heads", "heads", "heads")
                },
                onThrow = {
                    val cast = viewModel.throwCoins()
                    coinFaces = cast.coins
                    isFlipping = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ResultsContainer(isComplete, hexagram, inputState, actions.onNavigateToResult)
        }

        ThemeToggleButton(
            isDarkTheme = isDarkTheme,
            onToggleTheme = actions.onToggleTheme,
            modifier = Modifier.navigationBarsPadding()
        )

        HistoryButton(
            onOpenHistory = actions.onOpenHistory,
            modifier = Modifier.statusBarsPadding()
        )
    }

    LaunchedEffect(isFlipping) {
        if (isFlipping) {
            delay(UIConstants.COIN_FLIP_TOTAL_DURATION.milliseconds)
            isFlipping = false
        }
    }
}

@Composable
private fun VignetteOverlay() {
    val vignetteColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.15f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.Transparent, vignetteColor),
                    radius = 900f
                )
            )
    )
}

@Composable
private fun HeaderSection() {
    Text(
        text = stringResource(iching_chinese),
        fontFamily = ChakraPetch,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        letterSpacing = 6.sp,
        color = MaterialTheme.colorScheme.secondary
    )
    Spacer(Modifier.height(6.dp))
    Text(
        text = stringResource(book_of_changes),
        fontFamily = ChakraPetch,
        fontWeight = FontWeight.Light,
        fontSize = 28.sp,
        letterSpacing = 4.sp,
    )
}

@Composable
private fun CoinsSection(isFlipping: Boolean, coinFaces: List<String>, isDarkTheme: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        repeat(UIConstants.COIN_COUNT) { i ->
            Coin(
                isFlipping = isFlipping,
                result = coinFaces[i],
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
private fun ActionButtons(
    isComplete: Boolean,
    isFlipping: Boolean,
    coinThrowsCount: Int,
    onReset: () -> Unit,
    onThrow: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            onClick = { if (isComplete) onReset() else onThrow() },
            enabled = !isFlipping,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isComplete) stringResource(cast_again) else stringResource(throw_coins),
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                letterSpacing = 3.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth(UIConstants.BUTTON_PROGRESS_WIDTH_FRACTION)
                .align(Alignment.BottomCenter)
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth()
            )
            val progressWidth by animateFloatAsState(
                targetValue = coinThrowsCount / 6f,
                animationSpec = tween(UIConstants.PROGRESS_BAR_TWEEN_DURATION),
                label = "progress"
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth(progressWidth)
            )
        }
    }
}

@Composable
private fun ResultsContainer(
    isComplete: Boolean,
    hexagram: Hexagram?,
    inputState: TextFieldState,
    onNavigateToResult: (Hexagram, String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.height(60.dp)) {
            hexagram?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.hexagram_label, it.primaryNumber, it.primaryName),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = ChakraPetch,
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp,
                        letterSpacing = 4.sp,
                    )
                    if (it.hasChangingLines) {
                        it.relatingName?.let { name ->
                            Text(
                                text = stringResource(R.string.relating_hex_label, it.relatingNumber ?: 0, name),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.height(56.dp).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (isComplete) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth(UIConstants.BUTTON_PROGRESS_WIDTH_FRACTION)
                        .align(Alignment.TopCenter)
                )
                TextButton(
                    onClick = { hexagram?.let { onNavigateToResult(it, inputState.text.toString()) } },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.read_more),
                        fontFamily = ChakraPetch,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 3.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.ThemeToggleButton(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onToggleTheme,
        modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 24.dp)
    ) {
        Icon(
            painter = painterResource(if (isDarkTheme) R.drawable.ic_light_mode else R.drawable.dark),
            contentDescription = stringResource(
                if (isDarkTheme) R.string.switch_to_light_mode else R.string.switch_to_dark_mode
            ),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun BoxScope.HistoryButton(
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 16.dp)
            .align(Alignment.TopStart)
    ) {
        IconButton(
            onClick = onOpenHistory,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = stringResource(R.string.history_content_description),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true, name = "Dark")
@Composable
fun CastScreenDarkPreview() {
    IchingandroidTheme(darkTheme = true) { CastScreen(isDarkTheme = true) }
}

@Preview(showBackground = true, name = "Light")
@Composable
fun CastScreenLightPreview() {
    IchingandroidTheme(darkTheme = false) { CastScreen(isDarkTheme = false) }
}
