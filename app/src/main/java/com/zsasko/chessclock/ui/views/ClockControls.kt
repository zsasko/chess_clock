package com.zsasko.chessclock.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.chessclock.R
import com.zsasko.chessclock.model.ChessRuleset
import com.zsasko.chessclock.model.state.ChessGameplayUiState
import com.zsasko.chessclock.utils.DEFAULT_RULESETS
import com.zsasko.chessclock.utils.TEST_TAG_BUTTON_RESET
import com.zsasko.chessclock.utils.TEST_TAG_BUTTON_START_PAUSE

@Composable
fun ClockControls(
    appState: ChessGameplayUiState,
    selectedRuleset: ChessRuleset?,
    onStartClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onResetClicked: () -> Unit,
    onSelectRulesetClicked: () -> Unit,
    onCreateRulesetClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPlayActive = appState is ChessGameplayUiState.Running
    val isPlayPaused = appState is ChessGameplayUiState.Paused

    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                if (isPlayActive) {
                    onPauseClicked()
                } else {
                    onStartClicked()
                }
            }, modifier = Modifier.testTag(TEST_TAG_BUTTON_START_PAUSE)) {
                Text(
                    if (isPlayActive) stringResource(R.string.clock_controls_pause) else stringResource(
                        R.string.clock_controls_start
                    ),

                    style = MaterialTheme.typography.labelLarge
                )
            }
            Button(
                onClick = onResetClicked,
                enabled = isPlayActive,
                modifier = Modifier.testTag(TEST_TAG_BUTTON_RESET)
            ) {
                Text(
                    stringResource(R.string.clock_controls_reset),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        Button(
            onClick = onSelectRulesetClicked,
            enabled = !isPlayActive && !isPlayPaused
        ) {
            Text(
                stringResource(
                    R.string.clock_controls_ruleset_format,
                    selectedRuleset?.name.toString()
                ),
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Button(
            onClick = onCreateRulesetClicked,
            enabled = !isPlayActive && !isPlayPaused
        ) {
            Text(
                stringResource(R.string.clock_controls_create_new_ruleset),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
fun PreviewClockControls() {
    ClockControls(
        ChessGameplayUiState.Paused(),
        DEFAULT_RULESETS.first(),
        {},
        {},
        {},
        {},
        {})
}
