package com.zsasko.chessclock.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChessRuleset(
    val name: String,
    val maxPlayTimePerPlayerMs: Long,
    val timeIncrementAfterMoveIsMadeMs: Long
)