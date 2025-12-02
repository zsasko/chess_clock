package com.zsasko.chessclock.utils

import com.zsasko.chessclock.model.ChessRuleset

val DEFAULT_RULESETS = listOf(
    ChessRuleset(
        "Blitz (5+0)",
        maxPlayTimePerPlayerMs = 5 * 60_000L,
        timeIncrementAfterMoveIsMadeMs = 0L
    ),
    ChessRuleset(
        "Blitz (3+2)",
        maxPlayTimePerPlayerMs = 3 * 60_000L,
        timeIncrementAfterMoveIsMadeMs = 2_000L
    ),
    ChessRuleset(
        "Rapid (15+10)",
        maxPlayTimePerPlayerMs = 15 * 60_000L,
        timeIncrementAfterMoveIsMadeMs = 10_000L
    ),
    ChessRuleset(
        "Classical (60+0)",
        maxPlayTimePerPlayerMs = 60 * 60_000L,
        timeIncrementAfterMoveIsMadeMs = 0L
    ),
    ChessRuleset(
        "Custom",
        maxPlayTimePerPlayerMs = 10 * 60_000L,
        timeIncrementAfterMoveIsMadeMs = 0L
    ),
)

const val TICK_INTERVAL_MS = 100L