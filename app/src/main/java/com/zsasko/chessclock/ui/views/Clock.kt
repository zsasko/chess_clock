package com.zsasko.chessclock.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zsasko.chessclock.R
import java.util.Locale

@Composable
fun Clock(
    isPlayButtonActive: Boolean,
    playerTime: Long,
    onStopMyStartOtherClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTime = playerTime.formatMillis()
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                currentTime, style = MaterialTheme.typography.headlineLarge
            )
            Button(onStopMyStartOtherClicked, enabled = isPlayButtonActive) {
                Text(
                    stringResource(R.string.chess_gameplay_stop_my_start_other),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Formats the value in milliseconds into minutes and seconds.
 */
fun Long.formatMillis(): String {
    val totalSeconds = (this / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}

@Preview
@Composable
fun PreviewClock() {
    Clock(true, 0L, {}, Modifier)
}