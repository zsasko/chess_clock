package com.zsasko.chessclock.model.state

import com.zsasko.chessclock.model.Players

interface ChessGameplayUiState {
    class Initialized : ChessGameplayUiState
    class Running : ChessGameplayUiState
    class Paused : ChessGameplayUiState
    class Stopped : ChessGameplayUiState
    class Finished(val winner: Players) : ChessGameplayUiState
}
