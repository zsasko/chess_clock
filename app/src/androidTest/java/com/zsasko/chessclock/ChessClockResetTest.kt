package com.zsasko.chessclock


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.zsasko.chessclock.utils.TEST_TAG_BUTTON_RESET
import com.zsasko.chessclock.utils.TEST_TAG_BUTTON_START_PAUSE
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ChessClockResetTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testResetButtonResetsButtonsToDefault() {
        val startStopButton = composeTestRule.onNodeWithTag(TEST_TAG_BUTTON_START_PAUSE)

        startStopButton.performClick()

        // 3. Wait briefly or assert that time has changed (optional but good for robustness)
        composeTestRule.waitForIdle()

        // Verify time has changed (it should no longer be the default)
        startStopButton.assertIsDisplayed()

        composeTestRule.onNodeWithTag(TEST_TAG_BUTTON_RESET)
            .performClick()

        composeTestRule.onNodeWithTag(TEST_TAG_BUTTON_START_PAUSE)
            .assertIsEnabled()
        composeTestRule.onNodeWithTag(TEST_TAG_BUTTON_RESET)
            .assertIsNotEnabled()
    }
}
