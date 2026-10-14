package com.example.dessert.screens

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessert.GameViewModel
import com.example.dessert.R
import com.example.dessert.utils.TimeConvertor
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.dessert.data.ItemType
import com.example.dessert.data.Vector2
import kotlinx.coroutines.flow.MutableStateFlow

@Preview(showBackground = true)
@Composable
fun GamePage(onExit: () -> Unit = {}, onRestart: () -> Unit = {}, nextPage: () -> Unit = {}, viewModel: GameViewModel = viewModel()) {
    val gameState = viewModel.gameState.collectAsState().value
    val isPause = viewModel.isPause.collectAsState().value
    val isEnded = viewModel.isEnded.collectAsState().value
    val isClickExit = rememberSaveable { mutableStateOf(false) }
    val isClickRestart = rememberSaveable { mutableStateOf(false) }

    val itemsImage: List<ImageBitmap> = ItemType.entries.map {
        ImageBitmap.imageResource(id = it.image)
    }

    val playerImage: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.player)

    LaunchedEffect(isEnded || isPause) {
        var lastTimeNanos = 0L
        while (isEnded.not() && isPause.not()) {
            withFrameNanos { frameTimeNanos ->
                if (lastTimeNanos != 0L) {
                    val deltaTime = (frameTimeNanos - lastTimeNanos) / 1_000_000_000f
                    viewModel.updateGameState(deltaTime)
                }
                lastTimeNanos = frameTimeNanos
            }
        }
        if (isEnded) {
            nextPage()
        }
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StatsBoard(R.drawable.board, gameState.score.toString())
                    StatsBoard(R.drawable.life, "x" + gameState.hp)
                    Text(
                        text = "Time: " + TimeConvertor.floatToTime(gameState.remainingSeconds),
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight(500)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.stop),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(54.dp)
                        .clickable(onClick = {
                            viewModel.pauseGame(true)
                        })
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                if (isClickExit.value) {
                    AlertDialog(
                        title = {
                            Text(text = "Do you want leave?")
                        },
                        onDismissRequest = {
                            isClickExit.value = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    onExit()
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    isClickExit.value = false
                                }
                            ) {
                                Text("No")
                            }
                        }
                    )
                } else if (isClickRestart.value) {
                    AlertDialog(
                        title = {
                            Text(text = "Do you want restart game?")
                        },
                        onDismissRequest = {
                            isClickRestart.value = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.restartGame()
                                    onRestart()
                                }
                            ) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    isClickRestart.value = false
                                }
                            ) {
                                Text("No")
                            }
                        }
                    )
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned{ coordinates ->
                            viewModel.initScreenSize(
                                coordinates.size.width.toFloat(),
                                coordinates.size.height.toFloat()
                            )
                        }
                        .clickable(
                            onClick = {
                                if (gameState.playerVelocity.x > 0) {
                                    viewModel.setVelocity(Vector2(-300f, 0f))
                                } else {
                                    viewModel.setVelocity(Vector2(300f, 0f))
                                }
                            }
                        )
                ) {
                    scale(scaleX = if (gameState.playerVelocity.x > 0) {
                        -1f
                    } else {
                        1f
                    },
                        scaleY = 1f, pivot = Offset(gameState.playerPosition.x + 175f, 0f)) {
                        drawImage(
                            image = playerImage,
                            dstOffset = IntOffset(gameState.playerPosition.x.toInt(), gameState.playerPosition.y.toInt()),
                            dstSize = IntSize(350, 350)
                        )
                    }

                    gameState.items.forEach { item ->
                        drawImage(
                            image = itemsImage[item.type.ordinal],
                            dstOffset = IntOffset(item.position.x.toInt(), item.position.y.toInt()),
                            dstSize = IntSize(150, 150)
                        )
                    }
                }
                if (isPause) {
                    Box(
                        modifier = Modifier
                            .background(Color(0f,0f,0f, 0.6f))
                            .fillMaxSize()
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.play),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.clickable {
                                viewModel.pauseGame(false)
                            }.size(80.dp)
                        )

                        Image(
                            painter = painterResource(id = R.drawable.restart),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.clickable {
                                isClickRestart.value = true
                            }.size(80.dp)

                        )

                        Image(
                            painter = painterResource(id = R.drawable.out),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.clickable {
                                isClickExit.value = true
                            }.size(80.dp)
                        )
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.bottom_bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun StatsBoard(@DrawableRes imageId: Int, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.title_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(24.dp, 10.dp)
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = value,
                fontSize = 30.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(600)
            )
        }
    }
}