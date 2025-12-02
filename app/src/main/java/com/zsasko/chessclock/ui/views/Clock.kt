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
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Clock(
    isPlayActive: Boolean,
    currentTime: String, onStopMyStartOtherClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(currentTime, style = MaterialTheme.typography.headlineLarge)

            Button(onStopMyStartOtherClicked, enabled = isPlayActive) {
                Text("Stop My Start Other")
            }
        }
    }
}

@Preview
@Composable
fun PreviewClock() {
    Clock(false, "05:23") {}
}