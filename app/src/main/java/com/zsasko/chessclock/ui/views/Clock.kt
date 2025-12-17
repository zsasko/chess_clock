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
import com.zsasko.chessclock.model.Players
import com.zsasko.chessclock.model.state.ChessGameplayUiState
import com.zsasko.chessclock.model.state.GameplayData
import com.zsasko.chessclock.utils.formatMillis

@Composable
fun Clock(
    appState: ChessGameplayUiState,
    clockForPlayer: Players,
    onStopMyStartOtherClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPlayActive = appState is ChessGameplayUiState.Running && appState.data.selectedPlayer == clockForPlayer
    val currentTime = if (clockForPlayer == Players.FIRST) appState.data.firstPlayerDisplayTime.formatMillis() else  appState.data.secondPlayerDisplayTime.formatMillis()

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
    Clock(ChessGameplayUiState.Paused(GameplayData()), Players.FIRST, {}, Modifier)
}