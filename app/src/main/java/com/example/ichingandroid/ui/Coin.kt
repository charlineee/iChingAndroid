package com.example.ichingandroid.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ichingandroid.R
import kotlin.math.roundToInt

@Composable
fun Coin(
    isFlipping: Boolean,
    result: String?,
    modifier: Modifier = Modifier
) {
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(isFlipping) {
        if (isFlipping) {
            // Arc up
            offsetY.animateTo(
                targetValue = -500f,
                animationSpec = tween(250, easing = FastOutLinearInEasing)
            )
            // Arc down no spring
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(350, easing = LinearOutSlowInEasing)
            )
        } else {
            offsetY.snapTo(0f)
        }
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.catflipcoin2x)
    )

    Box(
        modifier = modifier
            .size(110.dp)
            .offset { IntOffset(0, offsetY.value.roundToInt()) }
    ) {
        if (isFlipping) {
            LottieAnimation(
                composition = composition,
                speed = 4f,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(
                    if (result == "tails") R.drawable.tail5 else R.drawable.heads1
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}