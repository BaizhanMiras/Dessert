package com.example.dessert.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessert.GameViewModel
import com.example.dessert.R
import com.example.dessert.data.User
import com.example.dessert.utils.TimeConvertor

@SuppressLint("SuspiciousIndentation")
@Preview(showBackground = true)
@Composable
fun GameOverPage(onclick: () -> Unit = {}, saveData: (User) -> Unit = {}, viewModel: GameViewModel = viewModel()) {
    val gameState = viewModel.gameState.collectAsState().value

        Column(
            modifier = Modifier.fillMaxHeight().padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game Over",
                fontSize = 42.sp,
                lineHeight = 36.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(700)
            )

            gameState.caughtCounts.forEach { (itemType, count) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Image(
                        painter = painterResource(id = itemType.boxImage),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(80.dp)
                    )
                    Text(
                        text = "x$count",
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight(500)
                    )
                    Text(
                        text = "+" + itemType.points * count,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "TIME",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(700)
                )
                Text(
                    text = TimeConvertor.floatToTime(gameState.elapsedSeconds),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "SCORE",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(700)
                )
                Text(
                    text = gameState.score.toString(),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.weight(1f)
                )
            }
            Button(
                onClick = {
                    onclick()
                    saveData(User(name = gameState.playerName, time = gameState.elapsedSeconds, score = gameState.score))
                }
            ) {
                Text(
                    text = "Next ->",
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(500)
                )
            }
        }

}

