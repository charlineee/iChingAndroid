package com.example.ichingandroid.ui

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.ichingandroid.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ichingandroid.data.ReadingEntity
import com.example.ichingandroid.ui.theme.ChakraPetch

@Composable
fun HistoryDrawerContent(
    viewModel: HistoryViewModel,
    onReadingClick: (ReadingEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val readings by viewModel.readings.collectAsState()

    ModalDrawerSheet(
        modifier = modifier.width(300.dp).fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.history_title),
                    fontFamily = ChakraPetch,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    letterSpacing = 2.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            Spacer(Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(readings, key = { it.id }) { reading ->
                    HistoryItem(
                        reading = reading,
                        onClick = { onReadingClick(reading) },
                        onDelete = { viewModel.deleteReading(reading) }
                    )
                }
            }

            if (readings.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.clear_history),
                    fontFamily = ChakraPetch,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.clearHistory() }
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun HistoryItem(
    reading: ReadingEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val timeRelative = remember(reading.timestamp) {
        DateUtils.getRelativeTimeSpanString(reading.timestamp).toString()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = reading.question.ifBlank { stringResource(R.string.no_question) },
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "$timeRelative • Hex ${reading.primaryHexNumber}",
                fontFamily = ChakraPetch,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_content_description),
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
        }
    }
}
