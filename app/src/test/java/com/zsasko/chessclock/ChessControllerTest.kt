package com.zsasko.chessclock

import com.zsasko.chessclock.controllers.ChessControllerImpl
import com.zsasko.chessclock.model.ChessRuleset
import com.zsasko.chessclock.model.Players
import com.zsasko.chessclock.model.state.ChessGameplayUiState
import com.zsasko.chessclock.utils.DEFAULT_RULESETS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChessControllerImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var controller: ChessControllerImpl

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        controller = ChessControllerImpl(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is initialized with first default ruleset`() {
        val state = controller.appState().value
        val activeRuleset = controller.activeRuleset().value

        assertTrue(state is ChessGameplayUiState.Initialized)
        assertEquals(DEFAULT_RULESETS.first(), activeRuleset)
        assertEquals(
            DEFAULT_RULESETS.first().maxPlayTimePerPlayerMs,
            controller.firstPlayerDisplayTime().value
        )
        assertEquals(
            DEFAULT_RULESETS.first().maxPlayTimePerPlayerMs,
            controller.secondPlayerDisplayTime().value
        )
    }

    @Test
    fun `resetPlay pauses and resets times and state`() = runTest {
        val ruleset = ChessRuleset("Test", 5000L, 500L)
        controller.setRuleset(ruleset)
        controller.startPlay(Players.FIRST)
        advanceTimeBy(2000L)

        controller.resetPlay()

        assertEquals(ruleset.maxPlayTimePerPlayerMs, controller.firstPlayerDisplayTime().value)
        assertEquals(ruleset.maxPlayTimePerPlayerMs, controller.secondPlayerDisplayTime().value)
        assertTrue(controller.appState().value is ChessGameplayUiState.Initialized)
    }

    @Test
    fun `calculateIfGameIsOver triggers finished state`() = runTest {
        val ruleset = ChessRuleset("Test", 100L, 0L)
        controller.setRuleset(ruleset)

        controller.startPlay(Players.FIRST)
        advanceTimeBy(200L) // advance past max time
        controller.calculateIfGameIsOver()

        val state = controller.appState().value
        assertTrue(state is ChessGameplayUiState.Finished)
        assertEquals(Players.SECOND, (state as ChessGameplayUiState.Finished).winner)
    }

}
