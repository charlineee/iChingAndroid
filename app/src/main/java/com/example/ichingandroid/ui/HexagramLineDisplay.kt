package com.example.ichingandroid.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ichingandroid.R
import com.example.ichingandroid.model.CoinThrow
import com.example.ichingandroid.model.LineType
import com.example.ichingandroid.ui.theme.ChangingLineTint

private val yangLines = listOf(R.drawable.yangline1, R.drawable.yangline2)
private val yinLines  = listOf(R.drawable.yinline1,  R.drawable.yinline2)

@Composable
fun HexagramLineDisplay(
    throws: List<CoinThrow>,
    modifier: Modifier = Modifier,
    isComplete: Boolean = false
) {
    val normalTint = MaterialTheme.colorScheme.onBackground

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Bottom),
        modifier = modifier.fillMaxSize()
    ) {
        throws.reversed().forEach { t ->
            val drawable = when (t.lineType) {
                LineType.YANG,
                LineType.YANG_CHANGING -> yangLines[t.drawableVariant]
                LineType.YIN,
                LineType.YIN_CHANGING  -> yinLines[t.drawableVariant]
            }
            val tint = if (isComplete && t.lineType.isChanging()) ChangingLineTint else normalTint

            Image(
                painter = painterResource(drawable),
                contentDescription = t.lineType.name,
                colorFilter = ColorFilter.tint(tint),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(24.dp)
            )
        }
    }
}