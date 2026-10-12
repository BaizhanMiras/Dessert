package com.example.dessert

import androidx.lifecycle.ViewModel
import com.example.dessert.data.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.max

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun updateGameState(deltaTime: Float) {
        _gameState.update { currentState ->
            currentState.copy(
                elapsedSeconds = currentState.elapsedSeconds + deltaTime,
                remainingSeconds = max(0f, currentState.remainingSeconds - deltaTime)
            )
        }
    }
}