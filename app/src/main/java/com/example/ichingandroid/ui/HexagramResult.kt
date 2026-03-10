package com.example.ichingandroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ichingandroid.model.Hexagram

@Composable
fun HexagramResult(
    hexagram: Hexagram,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "Hexagram ${hexagram.primaryNumber}: ${hexagram.primaryName}",
            color = MaterialTheme.colorScheme.onBackground
        )
        if (hexagram.hasChangingLines) {
            hexagram.relatingName?.let { name ->
                Text(
                    text = "→ ${hexagram.relatingNumber}: $name",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}