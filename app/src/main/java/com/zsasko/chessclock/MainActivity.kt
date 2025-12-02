package com.zsasko.chessclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.zsasko.chessclock.ui.screens.ChessGameplayScreen
import com.zsasko.chessclock.ui.theme.ChessClockTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChessClockTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChessGameplayScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}