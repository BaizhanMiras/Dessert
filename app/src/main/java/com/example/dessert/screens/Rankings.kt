package com.example.dessert.screens
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun RankingsPage(onExit: () -> Unit = {}, list: List<User> = emptyList(), viewModel: GameViewModel = viewModel()) {

        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rankings",
                fontSize = 38.sp,
                lineHeight = 36.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(700)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Rank",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(700)
                )
                Text(
                    text = "Name",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(700)
                )
                Text(
                    text = "Time",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(700)
                )
                Text(
                    text = "Score",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(700)
                )
            }

            Column (
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) {
                val sortedList = list.sortedByDescending { it.score }
                sortedList.forEachIndexed { index, user ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight(700)
                        )
                        Text(
                            text = user.name,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight(700)
                        )
                        Text(
                            text = TimeConvertor.floatToTime(user.time),
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight(700)
                        )
                        Text(
                            text = user.score.toString(),
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight(700)
                        )
                    }
                    }
            }

            Text(
                text = "-- Tab to Retry --",
                fontSize = 32.sp,
                fontWeight = FontWeight(700),
                modifier = Modifier.clickable {
                    viewModel.restartGame()
                    onExit()
                }
            )
        }
}