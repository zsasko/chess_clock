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

@Composable
fun Clock(
    isPlayActive: Boolean,
    currentTime: String,
    onStopMyStartOtherClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                currentTime, style = MaterialTheme.typography.headlineLarge
            )
            Button(onStopMyStartOtherClicked, enabled = isPlayActive) {
                Text(
                    stringResource(R.string.chess_gameplay_stop_my_start_other),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewClock() {
    Clock(false, "05:23", {}, Modifier)
}