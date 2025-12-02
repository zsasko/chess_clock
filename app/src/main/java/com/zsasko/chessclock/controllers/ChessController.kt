package com.zsasko.chessclock.controllers

import com.zsasko.chessclock.model.ChessRuleset
import com.zsasko.chessclock.model.Players
import com.zsasko.chessclock.model.state.ChessGameplayUiState
import com.zsasko.chessclock.utils.DEFAULT_RULESETS
import com.zsasko.chessclock.utils.TICK_INTERVAL_MS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ChessController {
    fun appState(): StateFlow<ChessGameplayUiState>
    suspend fun startPlay(currentPlayer: Players)
    fun stopMyStartOther()
    fun stopPlay()
    fun pausePlay()
    fun resetPlay()
    fun firstPlayerDisplayTime(): StateFlow<Long>
    fun secondPlayerDisplayTime(): StateFlow<Long>

    fun setRuleset(ruleset: ChessRuleset)
    fun createNewRuleset(ruleset: ChessRuleset)
    fun activeRuleset(): StateFlow<ChessRuleset?>
    fun allRulesets(): StateFlow<List<ChessRuleset>>
    fun setCustomTimes(baseMillis: Long, incrementMillis: Long)

    fun dispose()
}

class ChessControllerImpl(val dispatcher: CoroutineDispatcher) : ChessController {
    private val _currentPlayer = MutableStateFlow<Players?>(null)
    private var _firstPlayerTimeInSec = MutableStateFlow<Long>(0)
    private var _secondPlayerTimeInSec = MutableStateFlow<Long>(0)

    private var _rulesets = MutableStateFlow(DEFAULT_RULESETS)
    private var _currentRuleset = MutableStateFlow<ChessRuleset?>(null)

    private var _appState =
        MutableStateFlow<ChessGameplayUiState>(ChessGameplayUiState.Initialized())

    private var ticker: Job? = null

    init {
        setRuleset(_rulesets.value.first())
    }

    override fun setRuleset(ruleset: ChessRuleset) {
        _currentRuleset.value = ruleset
        resetClock(ruleset.maxPlayTimePerPlayerMs)
    }

    override fun createNewRuleset(ruleset: ChessRuleset) {
        _rulesets.value += ruleset
    }

    private fun resetClock(maxTime: Long) {
        _firstPlayerTimeInSec.value = maxTime
        _secondPlayerTimeInSec.value = maxTime
    }

    override suspend fun startPlay(currentPlayer: Players) {
        if (ticker?.isActive == true) {
            return
        }
        if (_currentPlayer.value == null) {
            _currentPlayer.value = currentPlayer
        }
        _appState.value = ChessGameplayUiState.Running()
        startTicker()
    }

    private suspend fun startTicker() {
        withContext(dispatcher) {
            ticker = launch {
                while (_appState.value is ChessGameplayUiState.Running) {
                    if (_currentPlayer.value == Players.LEFT_YELLOW) {
                        _firstPlayerTimeInSec.value -= TICK_INTERVAL_MS
                    } else {
                        _secondPlayerTimeInSec.value -= TICK_INTERVAL_MS
                    }
                    delay(TICK_INTERVAL_MS)
                    calculateIfGameIsOver()
                }
            }
        }
    }

    override fun stopMyStartOther() {
        if (_appState.value !is ChessGameplayUiState.Running) return
        val opponent =
            if (_currentPlayer.value == Players.LEFT_YELLOW) Players.RIGHT_RED else Players.LEFT_YELLOW
        _currentPlayer.value = opponent
        applyIncrement()
    }

    fun applyIncrement() {
        val increment = _currentRuleset.value?.timeIncrementAfterMoveIsMadeMs ?: 0
        if (increment <= 0) return
        if (_currentPlayer.value == Players.RIGHT_RED) {
            _firstPlayerTimeInSec.value += increment
        } else {
            _secondPlayerTimeInSec.value += increment
        }
    }

    override fun stopPlay() {
        ticker?.cancel()
        _appState.value = ChessGameplayUiState.Stopped()
        _currentRuleset.value?.let {
            resetClock(it.maxPlayTimePerPlayerMs)
        }
    }

    override fun pausePlay() {
        _appState.value = ChessGameplayUiState.Paused()
        ticker?.cancel()
    }

    override fun resetPlay() {
        pausePlay()
        val ruleset = _currentRuleset.value ?: _rulesets.value.first()
        val maxTime = ruleset.maxPlayTimePerPlayerMs
        _firstPlayerTimeInSec.value = maxTime
        _secondPlayerTimeInSec.value = maxTime
        _currentPlayer.value = null
        _appState.value = ChessGameplayUiState.Initialized()
    }

    override fun firstPlayerDisplayTime(): StateFlow<Long> {
        return _firstPlayerTimeInSec.asStateFlow()
    }

    override fun secondPlayerDisplayTime(): StateFlow<Long> {
        return _secondPlayerTimeInSec.asStateFlow()
    }

    override fun activeRuleset(): StateFlow<ChessRuleset?> {
        return _currentRuleset.asStateFlow()
    }

    override fun allRulesets(): StateFlow<List<ChessRuleset>> {
        return _rulesets.asStateFlow()
    }

    override fun appState(): StateFlow<ChessGameplayUiState> {
        return _appState.asStateFlow()
    }

    override fun setCustomTimes(baseMillis: Long, incrementMillis: Long) {
        val newRuleset = ChessRuleset("Custom", baseMillis, incrementMillis)
        setRuleset(newRuleset)
    }

    override fun dispose() {
        stopPlay()
    }

    fun calculateIfGameIsOver() {
        if (_firstPlayerTimeInSec.value <= 0) {
            resetPlay()
            _appState.value = ChessGameplayUiState.Finished(Players.RIGHT_RED)
        }
        if (_secondPlayerTimeInSec.value <= 0) {
            resetPlay()
            _appState.value = ChessGameplayUiState.Finished(Players.LEFT_YELLOW)
        }
    }

}
