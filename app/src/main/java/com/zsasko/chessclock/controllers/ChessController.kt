package com.zsasko.chessclock.controllers

import com.zsasko.chessclock.model.ChessRuleset
import com.zsasko.chessclock.model.Players
import com.zsasko.chessclock.model.state.ChessGameplayUiState
import com.zsasko.chessclock.model.state.withData
import com.zsasko.chessclock.utils.DEFAULT_RULESETS
import com.zsasko.chessclock.utils.TICK_INTERVAL_MS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ChessRulesets {
    fun setRuleset(ruleset: ChessRuleset)
    fun createNewRuleset(ruleset: ChessRuleset)
    fun allRulesets(): StateFlow<List<ChessRuleset>>
}

interface ChessGameplay {
    suspend fun startPlay(currentPlayer: Players)
    fun stopMyStartOther()
    fun stopPlay()
    fun pausePlay()
    fun resetPlay()
}

interface ChessState {
    fun appState(): StateFlow<ChessGameplayUiState>
    fun dispose()
}

interface ChessController : ChessState, ChessGameplay, ChessRulesets

class ChessControllerImpl(val dispatcher: CoroutineDispatcher) : ChessController {

    private val _rulesets = MutableStateFlow(DEFAULT_RULESETS)

    private val _appState =
        MutableStateFlow<ChessGameplayUiState>(ChessGameplayUiState.Initialized())

    private var ticker: Job? = null

    init {
        setRuleset(_rulesets.value.first())
    }

    override fun setRuleset(ruleset: ChessRuleset) {
        _appState.update { state ->
            state.withData {
                it.copy(selectedRuleset = ruleset)
            }
        }
        resetClock(ruleset.maxPlayTimePerPlayerMs)
    }

    override fun createNewRuleset(ruleset: ChessRuleset) {
        _rulesets.update { it.add(ruleset) }
    }

    private fun resetClock(maxTime: Long) {
        _appState.update { state ->
            state.withData {
                it.copy(
                    firstPlayerDisplayTime = maxTime,
                    secondPlayerDisplayTime = maxTime
                )
            }
        }
    }

    override suspend fun startPlay(currentPlayer: Players) {
        if (ticker?.isActive == true) {
            return
        }
        if (_appState.value.data.selectedPlayer == null) {
            _appState.update { state ->
                state.withData {
                    it.copy(selectedPlayer = currentPlayer)
                }
            }
        }
        _appState.value = ChessGameplayUiState.Running(_appState.value.data)
        startTicker()
    }

    private suspend fun startTicker() {
        withContext(dispatcher) {
            ticker = launch {
                while (_appState.value is ChessGameplayUiState.Running) {
                    if (_appState.value.data.selectedPlayer == Players.FIRST) {
                        val newValue =
                            _appState.value.data.firstPlayerDisplayTime - TICK_INTERVAL_MS
                        _appState.update { state ->
                            state.withData {
                                it.copy(firstPlayerDisplayTime = newValue)
                            }
                        }
                    } else {
                        val newValue =
                            _appState.value.data.secondPlayerDisplayTime - TICK_INTERVAL_MS
                        _appState.update { state ->
                            state.withData {
                                it.copy(secondPlayerDisplayTime = newValue)
                            }
                        }
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
            if (_appState.value.data.selectedPlayer == Players.FIRST) Players.SECOND else Players.FIRST
        _appState.update { state ->
            state.withData {
                it.copy(selectedPlayer = opponent)
            }
        }
        applyIncrement()
    }

    fun applyIncrement() {
        val increment = _appState.value.data.selectedRuleset?.timeIncrementAfterMoveIsMadeMs ?: 0
        if (increment <= 0) return
        _appState.update { state ->
            val secondPlayerActive = _appState.value.data.selectedPlayer == Players.SECOND
            state.withData {
                if (secondPlayerActive) {
                    it.copy(firstPlayerDisplayTime = it.firstPlayerDisplayTime + increment)
                } else {
                    it.copy(secondPlayerDisplayTime = it.secondPlayerDisplayTime + increment)
                }
            }
        }
    }

    override fun stopPlay() {
        ticker?.cancel()
        _appState.update {
            ChessGameplayUiState.Stopped(it.data)
        }
        resetClock(_appState.value.data.selectedRuleset?.maxPlayTimePerPlayerMs ?: 0L)
    }

    override fun pausePlay() {
        _appState.update {
            ChessGameplayUiState.Paused(it.data)
        }
        ticker?.cancel()
    }

    override fun resetPlay() {
        pausePlay()
        val ruleset = _appState.value.data.selectedRuleset ?: _rulesets.value.first()
        val maxTime = ruleset.maxPlayTimePerPlayerMs
        _appState.update {
            ChessGameplayUiState.Initialized(it.data.copy(selectedPlayer = null))
        }
        resetClock(maxTime)
    }

    override fun allRulesets(): StateFlow<List<ChessRuleset>> {
        return _rulesets.asStateFlow()
    }

    override fun appState(): StateFlow<ChessGameplayUiState> {
        return _appState.asStateFlow()
    }

    override fun dispose() {
        stopPlay()
    }

    fun calculateIfGameIsOver() {
        if (_appState.value.data.firstPlayerDisplayTime <= 0) {
            resetPlay()
            _appState.update {
                ChessGameplayUiState.Finished(it.data, Players.SECOND)
            }
        }
        if (_appState.value.data.secondPlayerDisplayTime <= 0) {
            resetPlay()
            _appState.update {
                ChessGameplayUiState.Finished(it.data, Players.FIRST)
            }
        }
    }

}
