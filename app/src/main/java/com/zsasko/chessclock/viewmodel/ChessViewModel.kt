package com.zsasko.chessclock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsasko.chessclock.controllers.ChessController
import com.zsasko.chessclock.model.ChessRuleset
import com.zsasko.chessclock.model.Players
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChessViewModel @Inject constructor(private val chessController: ChessController) :
    ViewModel() {

    val allRulesets = chessController.allRulesets()
    val appState = chessController.appState()

    val selectedPlayer = appState
        .map { it.data.selectedPlayer }
        .distinctUntilChanged()

    val secondPlayerTime = appState
        .map { it.data.secondPlayerDisplayTime }
        .distinctUntilChanged()

    val firstPlayerTime = appState
        .map { it.data.firstPlayerDisplayTime }
        .distinctUntilChanged()

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