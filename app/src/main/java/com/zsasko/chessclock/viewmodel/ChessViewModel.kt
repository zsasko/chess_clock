package com.zsasko.chessclock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsasko.chessclock.controllers.ChessController
import com.zsasko.chessclock.model.ChessRuleset
import com.zsasko.chessclock.model.Players
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChessViewModel @Inject constructor(private val chessController: ChessController) :
    ViewModel() {

    val firstPlayerTimeInSec = chessController.firstPlayerDisplayTime()
    val secondPlayerTimeInSec = chessController.secondPlayerDisplayTime()
    val ruleset = chessController.activeRuleset()
    val allRulesets = chessController.allRulesets()
    val appState = chessController.appState()

    fun startPlay(currentPlayer: Players = Players.FIRST) {
        viewModelScope.launch {
            chessController.startPlay(currentPlayer)
        }
    }

    fun stopMyStartOther(players: Players) = chessController.stopMyStartOther(players)

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