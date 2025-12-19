package com.zsasko.chessclock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsasko.chessclock.controllers.ChessController
import com.zsasko.chessclock.model.ChessRuleset
import com.zsasko.chessclock.model.Players
import com.zsasko.chessclock.utils.DEFAULT_RULESETS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ChessViewModel @Inject constructor(private val chessController: ChessController) :
    ViewModel() {

    val allRulesets = chessController.allRulesets()
    val appState = chessController.appState()

    val selectedPlayer = appState
        .map { it.data.selectedPlayer }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = Players.FIRST,
        )

    val secondPlayerTime = appState
        .map { it.data.secondPlayerDisplayTime }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = 0,
        )

    val firstPlayerTime = appState
        .map { it.data.firstPlayerDisplayTime }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = 0,
        )

    val activeRuleset = appState
        .map { it.data.selectedRuleset }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = DEFAULT_RULESETS[0],
        )

    fun startPlay(currentPlayer: Players = Players.FIRST) {
        viewModelScope.launch {
            chessController.startPlay(currentPlayer)
        }
    }

    fun stopMyStartOther() = chessController.stopMyStartOther()

    fun resetPlay() = chessController.resetPlay()

    fun pausePlay() = chessController.pausePlay()

    fun setRuleset(ruleset: ChessRuleset) {
        viewModelScope.launch {
            chessController.stopPlay()
            chessController.setRuleset(ruleset)
        }
    }

    fun createNewRuleset(name: String, baseMs: Long, incrementMs: Long) {
        val newRuleset = ChessRuleset(name, baseMs, incrementMs)
        viewModelScope.launch {
            chessController.stopPlay()
            chessController.createNewRuleset(newRuleset)
            chessController.setRuleset(newRuleset)
        }
    }

    override fun onCleared() {
        chessController.dispose()
        super.onCleared()
    }
}