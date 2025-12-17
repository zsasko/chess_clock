package com.zsasko.chessclock.model.state

import com.zsasko.chessclock.model.Players

sealed interface ChessGameplayUiState {
    object Initialized : ChessGameplayUiState
    object Running : ChessGameplayUiState
    object Paused : ChessGameplayUiState
    object Stopped : ChessGameplayUiState
    class Finished(val winner: Players) : ChessGameplayUiState
}
