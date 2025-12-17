package com.zsasko.chessclock.model.state

import com.zsasko.chessclock.model.ChessRuleset
import com.zsasko.chessclock.model.Players

data class GameplayData(
    val firstPlayerDisplayTime: Long = 0L,
    val secondPlayerDisplayTime: Long = 0L,
    val selectedRuleset: ChessRuleset? = null,
    val selectedPlayer: Players? = null
)

sealed interface ChessGameplayUiState {
    val data: GameplayData

    data class Initialized(
        override val data: GameplayData = GameplayData()
    ) : ChessGameplayUiState

    data class Running(
        override val data: GameplayData
    ) : ChessGameplayUiState

    data class Paused(
        override val data: GameplayData
    ) : ChessGameplayUiState

    data class Stopped(
        override val data: GameplayData
    ) : ChessGameplayUiState

    data class Finished(
        override val data: GameplayData,
        val winner: Players
    ) : ChessGameplayUiState
}

/**
 * Sets the [GameplayData] to all [ChessGameplayUiState] states.
 */
fun ChessGameplayUiState.withData(transform: (GameplayData) -> GameplayData ): ChessGameplayUiState
{
    return when(this) {
        is ChessGameplayUiState.Running -> copy(data = transform(data))
        is ChessGameplayUiState.Paused -> copy(data = transform(data))
        is ChessGameplayUiState.Initialized -> copy(data = transform(data))
        is ChessGameplayUiState.Stopped -> copy(data = transform(data))
        is ChessGameplayUiState.Finished -> copy(data = transform(data))
    }
}