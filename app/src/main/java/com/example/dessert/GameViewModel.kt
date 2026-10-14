package com.example.dessert

import androidx.lifecycle.ViewModel
import com.example.dessert.data.FallingItem
import com.example.dessert.data.GameState
import com.example.dessert.data.ItemType
import com.example.dessert.data.Vector2
import com.example.dessert.data.add
import com.example.dessert.data.scale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.max

val COLLIDER_SIZE = Vector2(150f, 150f)
val PLAYER_SIZE = Vector2(350f, 350f)
val SPAWN_ITEM_RATE = 3f

fun isCollide(position: Vector2, otherPosition: Vector2): Boolean {
    if (
        position.x < otherPosition.x + PLAYER_SIZE.x &&
        position.x + COLLIDER_SIZE.x > otherPosition.x &&
        position.y < otherPosition.y + PLAYER_SIZE.y &&
        position.y + COLLIDER_SIZE.y > otherPosition.y
            ) {
        return true
    }
    return false
}



class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    private val _timer = MutableStateFlow(0f)
    private val _isPause = MutableStateFlow(false)
    val isPause = _isPause.asStateFlow()
    private val _isEnded = MutableStateFlow(false)
    val isEnded = _isEnded.asStateFlow()
    private val _screenSize = MutableStateFlow(Vector2(0f, 0f))

    private fun addRandomItem() {
        val itemTypes = ItemType.entries
        _gameState.update { currentState ->
            currentState.copy(
                items = currentState.items + FallingItem(
                    0,
                    itemTypes.random(),
                    Vector2(
                        (0..(_screenSize.value.x - COLLIDER_SIZE.x).toInt()).random().toFloat(),
                        -100f
                    )
                )
            )
        }
    }

    private fun calculateItems(deltaTime: Float) {
        if (_timer.value > SPAWN_ITEM_RATE) {
            _timer.value = 0f
            addRandomItem()
        }

        val mutableItems = mutableListOf<FallingItem>()
        _gameState.value.items.forEach { item ->
            item.position = item.position.add(Vector2(0f, 200f * deltaTime))
            if (item.position.y > _screenSize.value.y) {
                return@forEach
            }
            if (isCollide(item.position, _gameState.value.playerPosition)) {
                takeDamage(item.type.hpLoss)
                getPoints(item.type.points)
                _gameState.update { currentState ->
                    currentState.copy(
                        remainingSeconds = currentState.remainingSeconds + item.type.extraSeconds,
                        caughtCounts = currentState.caughtCounts + (item.type to (currentState.caughtCounts[item.type] ?: 0) + 1)
                    )
                }
                return@forEach
            }
            mutableItems.add(item)
        }
        _gameState.update { currentState ->
            currentState.copy(
                items = mutableItems
            )
        }
    }

    fun updateGameState(deltaTime: Float) {
        if (_gameState.value.remainingSeconds <= 0f) {
            _isEnded.value = true
            return
        }
        if (_isPause.value) return

        _timer.update { currentState ->
            currentState + deltaTime
        }

        calculateItems(deltaTime)

        _gameState.update { currentState ->
            currentState.copy(
                playerPosition =
                    if (currentState.playerPosition.x >= 0 && currentState.playerPosition.x < _screenSize.value.x - PLAYER_SIZE.x)
                        currentState.playerPosition.add(currentState.playerVelocity.scale(deltaTime))
                    else if (currentState.playerPosition.x < 0)
                        Vector2(0f, currentState.playerPosition.y)
                    else Vector2(_screenSize.value.x - PLAYER_SIZE.x - 1f, currentState.playerPosition.y),
                elapsedSeconds = currentState.elapsedSeconds + deltaTime,
                remainingSeconds = max(0f, currentState.remainingSeconds - deltaTime)
            )
        }
    }

    private fun takeDamage(damage: Int) {
        _gameState.update { currentState ->
            currentState.copy(
                hp = max(0, currentState.hp - damage)
            )
        }
    }

    fun initScreenSize(width: Float, height: Float) {
        if (_screenSize.value.x == 0f && width > 0f) {
            _screenSize.value = Vector2(width, height)
            _gameState.update { currentState ->
                currentState.copy(
                    playerPosition = Vector2(width / 2 - PLAYER_SIZE.x / 2, height - PLAYER_SIZE.y)
                )
            }
        }
    }

    fun setVelocity(velocity: Vector2) {
        _gameState.update { currentState ->
            currentState.copy(
                playerVelocity = velocity
            )
        }
    }

    private fun getPoints(points: Int) {
        _gameState.update { currentState ->
            currentState.copy(
                score = currentState.score + points
            )
        }
    }

    fun setPlayerName(name: String) {
        _gameState.update { currentState ->
            currentState.copy(
                playerName = name
            )
        }
    }

    fun pauseGame(isPause: Boolean) {
        _isPause.value = isPause
    }

    fun restartGame() {
        _isEnded.value = false
        _isPause.value = false
        _gameState.update { currentState ->
            currentState.copy(
                playerPosition = Vector2(_screenSize.value.x / 2 - PLAYER_SIZE.x / 2, _screenSize.value.y - PLAYER_SIZE.y),
                playerVelocity = Vector2(0f, 0f),
                elapsedSeconds = 0f,
                remainingSeconds = 30f,
                score = 0,
                hp = 3,
                items = emptyList(),
                caughtCounts = emptyMap()
            )
        }
    }
}