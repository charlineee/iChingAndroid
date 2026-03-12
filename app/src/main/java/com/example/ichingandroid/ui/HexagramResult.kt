package com.example.ichingandroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ichingandroid.R
import com.example.ichingandroid.R.string.cast_again
import com.example.ichingandroid.ui.theme.ChakraPetch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HexagramResult(
    reading: HexagramResultViewModel.ReadingResult,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(cast_again),
                        fontFamily = ChakraPetch,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 3.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
        // primary hexagram
        Text(
            text = stringResource(R.string.hexagram_label, reading.primaryHex.hex, reading.primaryName),
            fontFamily = ChakraPetch,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            letterSpacing = 3.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = reading.primaryHex.tradChinese,
            fontFamily = ChakraPetch,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(24.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
        Spacer(Modifier.height(24.dp))

        // judgment
        Text(
            text = stringResource(R.string.the_judgment),
            fontFamily = ChakraPetch,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            letterSpacing = 4.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = reading.judgment,
            fontFamily = ChakraPetch,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // image
        Text(
            text = stringResource(R.string.image),
            fontFamily = ChakraPetch,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            letterSpacing = 4.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = reading.image,
            fontFamily = ChakraPetch,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        // changing lines
        if (reading.changingLines.isNotEmpty()) {
            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.changing_lines),
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                letterSpacing = 4.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))

            reading.changingLines.forEach { (lineNum, text) ->
                Text(
                    text = "Line $lineNum",
                    fontFamily = ChakraPetch,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = text,
                    fontFamily = ChakraPetch,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        // relating hexagram
        reading.relatingHex?.let { relating ->
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.changing_to),
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                letterSpacing = 4.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.hexagram_label, relating.hex, reading.relatingName ?: relating.english),
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = relating.tradChinese,
                fontFamily = ChakraPetch,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(24.dp))

            // relating judgment
            Text(
                text = stringResource(R.string.the_judgment),
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                letterSpacing = 4.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = relating.judgment.text,
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // relating image
            Text(
                text = stringResource(R.string.image),
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                letterSpacing = 4.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = relating.image.text,
                fontFamily = ChakraPetch,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(32.dp))
        }
    }
}