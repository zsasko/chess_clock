package com.zsasko.chessclock.model

data class ChessRuleset(
    val name: String,
    val maxPlayTimePerPlayerMs: Long,
    val timeIncrementAfterMoveIsMadeMs: Long
)