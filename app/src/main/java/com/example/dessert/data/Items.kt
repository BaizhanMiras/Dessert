package com.example.dessert.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.dessert.R

enum class ItemType(
    val points: Int,
    val extraSeconds: Int = 0,
    val hpLoss: Int = 0,
    @DrawableRes val image: Int = R.drawable.baguette,
    @DrawableRes val boxImage: Int = R.drawable.baguette_box
) {
    BAGUETTE(points = 10),
    MACARON(points = 20, image = R.drawable.macaron, boxImage = R.drawable.macaron_box),
    PUFF(points = 30, extraSeconds = 30, image = R.drawable.puff, boxImage = R.drawable.puff_box),
    FISHBONE(points = -20, hpLoss = 1, image = R.drawable.fishbone, boxImage = R.drawable.fishbone_box),
    BONE(points = -10, image = R.drawable.bone, boxImage = R.drawable.bone_box)
}

data class Vector2(val x: Float, val y: Float)

fun Vector2.add(other: Vector2) = Vector2(x + other.x, y + other.y)
fun Vector2.scale(factor: Float) = Vector2(x * factor, y * factor)

data class FallingItem(
    val id: Long,
    val type: ItemType,
    var position: Vector2
)

data class GameState(
    val playerName: String = "",
    val playerPosition: Vector2 = Vector2(110f, 800f),
    val playerVelocity: Vector2 = Vector2(0f, 0f),
    val hp: Int = 3,
    val score: Int = 0,
    val remainingSeconds: Float = 30f,
    val elapsedSeconds: Float = 0f,
    val items: List<FallingItem> = emptyList(),
    val caughtCounts: Map<ItemType, Int> = emptyMap(),
    val awardedHpMilestones: Int = 0,
    val speedLevel: Int = 0
)