package com.example.ichingandroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
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
    modifier: Modifier = Modifier,
    reading: HexagramResultViewModel.ReadingResult,
    isSaved: Boolean = false,
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            ResultTopBar(onBack)
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
            HexagramHeader(
                hexNumber = reading.primaryHex.hex,
                name = reading.primaryName,
                tradChinese = reading.primaryHex.tradChinese
            )

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
            Spacer(Modifier.height(24.dp))

            JudgmentSection(reading.judgment)

            Spacer(Modifier.height(24.dp))

            ImageSection(reading.image)

            if (reading.changingLines.isNotEmpty()) {
                ChangingLinesSection(reading.changingLines)
            }

            reading.relatingHex?.let { relating ->
                RelatingHexagramSection(
                    hexNumber = relating.hex,
                    name = reading.relatingName ?: relating.english,
                    tradChinese = relating.tradChinese,
                    judgment = relating.judgment.text,
                    image = relating.image.text
                )
            }

            Spacer(Modifier.height(32.dp))

            SaveButton(isSaved = isSaved, onSave = onSave)

            Spacer(Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultTopBar(onBack: () -> Unit) {
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
                    contentDescription = stringResource(R.string.back_content_description),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun HexagramHeader(hexNumber: Int, name: String, tradChinese: String) {
    Text(
        text = stringResource(R.string.hexagram_label, hexNumber, name),
        fontFamily = ChakraPetch,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        letterSpacing = 3.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        text = tradChinese,
        fontFamily = ChakraPetch,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    )
}

@Composable
private fun JudgmentSection(judgment: String) {
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
        text = judgment,
        fontFamily = ChakraPetch,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ImageSection(image: String) {
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
        text = image,
        fontFamily = ChakraPetch,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ChangingLinesSection(changingLines: Map<Int, String>) {
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

    changingLines.forEach { (lineNum, text) ->
        Text(
            text = stringResource(R.string.line_label, lineNum),
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

@Composable
private fun RelatingHexagramSection(
    hexNumber: Int,
    name: String,
    tradChinese: String,
    judgment: String,
    image: String
) {
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
        text = stringResource(R.string.hexagram_label, hexNumber, name),
        fontFamily = ChakraPetch,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 2.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        text = tradChinese,
        fontFamily = ChakraPetch,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    )

    Spacer(Modifier.height(24.dp))

    JudgmentSection(judgment)

    Spacer(Modifier.height(24.dp))

    ImageSection(image)
}

@Composable
private fun SaveButton(isSaved: Boolean, onSave: () -> Unit) {
    Button(
        onClick = onSave,
        enabled = !isSaved,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            Icons.Default.Save,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(if (isSaved) R.string.saved else R.string.save_reading),
            fontFamily = ChakraPetch,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
