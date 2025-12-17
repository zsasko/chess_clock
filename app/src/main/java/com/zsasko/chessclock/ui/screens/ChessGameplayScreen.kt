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
import androidx.compose.runtime.getValue
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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.DialogSceneStrategy.Companion.dialog
import androidx.navigation3.ui.NavDisplay
import com.zsasko.chessclock.R
import com.zsasko.chessclock.model.Players
import com.zsasko.chessclock.model.state.ChessGameplayUiState
import com.zsasko.chessclock.ui.dialogs.CreateRulesetDialog
import com.zsasko.chessclock.ui.dialogs.GenericTextDialog
import com.zsasko.chessclock.ui.dialogs.SelectRulesetDialog
import com.zsasko.chessclock.ui.theme.ChessClockTheme
import com.zsasko.chessclock.ui.views.Clock
import com.zsasko.chessclock.ui.views.ClockControls
import com.zsasko.chessclock.viewmodel.ChessViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
data object GameScreen : NavKey

@Serializable
data object AddNewRulesetScreen : NavKey

@Serializable
data object SelectRulesetScreen : NavKey

@Serializable
data class DisplayGenericTextDialog(val title: String, val text: String) : NavKey

@Composable
fun ChessGameplayScreen(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(GameScreen)
    val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        sceneStrategy = dialogStrategy,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<GameScreen> {
                GameScreen(
                    onCreateNewRuleButtonClicked = {
                        backStack.add(AddNewRulesetScreen)
                    },
                    onSelectRuleButtonClicked = {
                        backStack.add(SelectRulesetScreen)
                    },
                    onShowGenericDialog = { title, text ->
                        backStack.add(DisplayGenericTextDialog(title, text))
                    }
                )
            }
            entry<SelectRulesetScreen>(
                metadata = dialog()
            ) {
                SelectRulesetDialog({
                    backStack.removeLastOrNull()
                }, { i ->
                    backStack.removeLastOrNull()
                })
            }
            entry<AddNewRulesetScreen>(
                metadata = dialog()
            ) {
                CreateRulesetDialog({
                    backStack.removeLastOrNull()
                }, { name, baseMs, incrementMs ->
                    backStack.removeLastOrNull()
                })
            }
            entry<DisplayGenericTextDialog>(
                metadata = dialog()
            ) { key ->
                GenericTextDialog(title = key.title, text = key.text) {
                    backStack.removeLastOrNull()
                }
            }
        }
    )
}


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    chessViewModel: ChessViewModel = hiltViewModel(),
    onCreateNewRuleButtonClicked: () -> Unit,
    onSelectRuleButtonClicked: () -> Unit,
    onShowGenericDialog: (title: String, text: String) -> Unit
) {
    val appState = chessViewModel.appState.collectAsStateWithLifecycle()

    val selectedPlayer by chessViewModel.appState
        .map { it.data.selectedPlayer }
        .distinctUntilChanged()
        .collectAsStateWithLifecycle(initialValue = null)

    val secondPlayerTime by chessViewModel.appState
        .map { it.data.secondPlayerDisplayTime }
        .distinctUntilChanged()
        .collectAsStateWithLifecycle(initialValue = 0)

    val firstPlayerTime by chessViewModel.appState
        .map { it.data.firstPlayerDisplayTime }
        .distinctUntilChanged()
        .collectAsStateWithLifecycle(initialValue = 0)

    val isPlayer1Active =
        appState.value is ChessGameplayUiState.Running && selectedPlayer == Players.FIRST
    val isPlayer2Active =
        appState.value is ChessGameplayUiState.Running && selectedPlayer == Players.SECOND

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
                        isPlayButtonActive = isPlayer1Active,
                        playerTime = firstPlayerTime,
                        onStopMyStartOtherClicked = {
                            chessViewModel.stopMyStartOther()
                        }
                    )
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
                        isPlayButtonActive = isPlayer2Active,
                        playerTime = secondPlayerTime,
                        onStopMyStartOtherClicked = {
                            chessViewModel.stopMyStartOther()
                        }
                    )
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
                appState = appState.value,
                onResetClicked = {
                    chessViewModel.resetPlay()
                }, onStartClicked = {
                    chessViewModel.startPlay()
                }, onPauseClicked = {
                    chessViewModel.pausePlay()
                },
                onSelectRulesetClicked = {
                    onSelectRuleButtonClicked()
                },
                onCreateRulesetClicked = {
                    onCreateNewRuleButtonClicked()
                },
                selectedRuleset = appState.value.data.selectedRuleset
            )
        }
    }
    if (appState.value is ChessGameplayUiState.Finished) {
        val state = appState.value as ChessGameplayUiState.Finished
        onShowGenericDialog(
            stringResource(R.string.general_message),
            stringResource(R.string.chess_gameplay_winner_is, state.winner.name)
        )
        chessViewModel.resetPlay()
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