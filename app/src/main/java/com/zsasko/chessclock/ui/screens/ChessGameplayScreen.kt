package com.zsasko.chessclock.ui.screens

import android.content.res.Configuration
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zsasko.chessclock.R
import com.zsasko.chessclock.model.state.ChessGameplayUiState
import com.zsasko.chessclock.ui.dialogs.CreateRulesetDialog
import com.zsasko.chessclock.ui.dialogs.GenericTextDialog
import com.zsasko.chessclock.ui.dialogs.SelectRulesetDialog
import com.zsasko.chessclock.ui.theme.ChessClockTheme
import com.zsasko.chessclock.ui.views.Clock
import com.zsasko.chessclock.ui.views.ClockControls
import com.zsasko.chessclock.utils.formatMillis
import com.zsasko.chessclock.viewmodel.ChessViewModel

@Composable
fun ChessGameplayScreen(
    modifier: Modifier = Modifier,
    chessViewModel: ChessViewModel = hiltViewModel()
) {
    val firstPlayerTime = chessViewModel.firstPlayerTimeInSec.collectAsStateWithLifecycle()
    val secondPlayerTime = chessViewModel.secondPlayerTimeInSec.collectAsStateWithLifecycle()
    val ruleset = chessViewModel.ruleset.collectAsStateWithLifecycle()
    val allRulesets = chessViewModel.allRulesets.collectAsStateWithLifecycle()
    val appState = chessViewModel.appState.collectAsStateWithLifecycle()

    val isPlayActive = appState.value is ChessGameplayUiState.Running

    val showSelectRuleset = remember { mutableStateOf(false) }
    val showCreateNewRuleset = remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = if (isPortrait) {
                GridCells.Fixed(1)
            } else {
                GridCells.Fixed(2)
            },
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxSize(),
        ) {
            // Player 1
            item {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(Color.Yellow)
                ) {
                    Clock(
                        isPlayActive = isPlayActive,
                        firstPlayerTime.value.formatMillis(),
                        onStopMyStartOtherClicked = {
                            chessViewModel.stopMyStartOther()
                        })
                }
            }
            // Player 2
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .background(Color.Red)
                ) {
                    Clock(
                        isPlayActive = isPlayActive,
                        secondPlayerTime.value.formatMillis(),
                        onStopMyStartOtherClicked = {
                            chessViewModel.stopMyStartOther()
                        })
                }
            }
        }
        Column(
            modifier = Modifier
                .align(if (isPortrait) Alignment.Center else Alignment.TopCenter)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RectangleShape
                )
                .background(Color.LightGray)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClockControls(
                isPlayActive = isPlayActive,
                onResetClicked = {
                    chessViewModel.resetPlay()
                }, onStartClicked = {
                    chessViewModel.startPlay()
                }, onPauseClicked = {
                    chessViewModel.pausePlay()
                },
                onSelectRulesetClicked = {
                    showSelectRuleset.value = true
                },
                onCreateRulesetClicked = {
                    showCreateNewRuleset.value = true
                },
                selectedRuleset = ruleset.value
            )
        }
    }

    if (showSelectRuleset.value) {
        SelectRulesetDialog(
            onDismiss = {
                showSelectRuleset.value = false
            }, onItemSelected = {
                chessViewModel.setRuleset(it)
            },
            options = allRulesets.value,
            initial = ruleset.value
        )
    }
    if (showCreateNewRuleset.value) {
        CreateRulesetDialog(onDismiss = {
            showCreateNewRuleset.value = false
        }, onApply = { name, baseMs, incrementMs ->
            chessViewModel.createNewRuleset(name, baseMs, incrementMs)
        })
    }
    if (appState.value is ChessGameplayUiState.Finished) {
        val state = appState.value as ChessGameplayUiState.Finished
        GenericTextDialog(
            title = stringResource(R.string.general_message),
            text = stringResource(R.string.chess_gameplay_winner_is, state.winner.name),
            onDismiss = {
                chessViewModel.resetPlay()
            })
    }

    val view = LocalView.current
    DisposableEffect(view) {
        val window = (view.context as? android.app.Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChessGameplayScreenPreview() {
    ChessClockTheme {
        ChessGameplayScreen()
    }
}