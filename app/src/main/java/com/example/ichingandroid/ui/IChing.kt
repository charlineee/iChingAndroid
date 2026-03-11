package com.example.ichingandroid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ichingandroid.ui.theme.ChakraPetch
import com.example.ichingandroid.ui.theme.IchingandroidTheme
import com.example.ichingandroid.ui.theme.LineTint
import kotlinx.coroutines.delay
import com.example.ichingandroid.R.string.cast_again
import com.example.ichingandroid.R.string.throw_coins
import com.example.ichingandroid.R.string.iching_chinese
import com.example.ichingandroid.R.string.book_of_changes

@Composable
fun IChing(
    modifier: Modifier = Modifier,
    viewModel: IChingViewModel = viewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isDarkTheme: Boolean = true,
    onToggleTheme: () -> Unit = {}
) {
    var isFlipping by remember { mutableStateOf(false) }
    var coinFaces by remember { mutableStateOf(listOf("heads", "heads", "heads")) }
    val throws by viewModel.throws.collectAsState()
    val hexagram by viewModel.hexagram.collectAsState()
    val isComplete by viewModel.isComplete.collectAsState()

    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        // Vignette overlay
        val vignetteColor = if (isDarkTheme) LineTint.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.1f)
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Text(
                text = stringResource(iching_chinese),
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
                letterSpacing = 6.sp,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(book_of_changes),
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.Light,
                fontSize = 28.sp,
                letterSpacing = 4.sp,
            )
            Spacer(Modifier.height(10.dp))
            // Hexagram lines (bottom-up)
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                HexagramLineDisplay(throws = throws, isComplete = isComplete)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Coins
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                repeat(3) { i ->
                    Coin(
                        isFlipping = isFlipping,
                        result = coinFaces[i],
                        isDarkTheme = isDarkTheme
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Throw counter
            Text(
                text = "Throw ${throws.size} / 6",
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = {
                        if (isComplete) {
                            viewModel.reset()
                            coinFaces = listOf("heads", "heads", "heads")
                            return@TextButton
                        }
                        val cast = viewModel.throwCoins()
                        coinFaces = cast.coins
                        isFlipping = true
                    },
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
                // visually anchor button
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .align(Alignment.BottomCenter)
                )
            }

            // Hexagram result
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.height(60.dp)) {
                hexagram?.let { HexagramResult(hexagram = it) }
            }
        }

        // Theme toggle pinned to bottom center
        IconButton(
            onClick = onToggleTheme,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (isDarkTheme) R.drawable.ic_light_mode else R.drawable.dark
                ),
                contentDescription = if (isDarkTheme) "Switch to light mode" else "Switch to dark mode",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }

    LaunchedEffect(isFlipping) {
        if (isFlipping) {
            delay(600)
            isFlipping = false
        }
    }
}

@Preview(showBackground = true, name = "Dark")
@Composable
private fun IChingDarkPreview() {
    IchingandroidTheme(darkTheme = true) { IChing(isDarkTheme = true) }
}

@Preview(showBackground = true, name = "Light")
@Composable
private fun IChingLightPreview() {
    IchingandroidTheme(darkTheme = false) { IChing(isDarkTheme = false) }
}
