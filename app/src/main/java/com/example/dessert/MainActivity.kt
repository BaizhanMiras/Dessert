package com.example.dessert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.dessert.data.AppDatabase
import com.example.dessert.data.User
import com.example.dessert.screens.GameOverPage
import com.example.dessert.screens.GamePage
import com.example.dessert.screens.HomePage
import com.example.dessert.screens.RankingsPage
import com.example.dessert.screens.UsernamePage
import com.example.dessert.ui.theme.DessertTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name")
                .fallbackToDestructiveMigration()
                .build()
        setContent {
            DessertTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        db = db,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

enum class CurrentScreen {
    Home,
    Username,
    Game,
    GameOver,
    Rankings,
}

@Composable
fun App(db: AppDatabase, modifier: Modifier = Modifier) {
    val currentScreen = rememberSaveable { mutableStateOf(CurrentScreen.Home) }
    val userDao = db.userDao()

    val usersState = userDao.getAll().collectAsState(initial = emptyList())
    val users = usersState.value
    val coroutineScope = rememberCoroutineScope()

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bottom_bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxWidth().height(20.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.bottom_bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxWidth().height(20.dp)
            )
        }
        Box(
            modifier = Modifier.fillMaxSize().padding(vertical = 20.dp),
        ) {
            when (currentScreen.value) {
                CurrentScreen.Home -> HomePage(
                    onClick = { currentScreen.value = CurrentScreen.Username }
                )

                CurrentScreen.Username -> UsernamePage(
                    onClick = { currentScreen.value = CurrentScreen.Game }
                )

                CurrentScreen.Game -> GamePage(
                    onExit = { currentScreen.value = CurrentScreen.Rankings },
                    onRestart = { currentScreen.value = CurrentScreen.Game },
                    nextPage = { currentScreen.value = CurrentScreen.GameOver }
                )

                CurrentScreen.GameOver -> GameOverPage(
                    onclick = { currentScreen.value = CurrentScreen.Rankings },
                    saveData = { user ->
                        coroutineScope.launch {
                            userDao.insertAll(user)
                        }
                    }
                )

                CurrentScreen.Rankings -> RankingsPage(
                    onExit = { currentScreen.value = CurrentScreen.Home },
                    list = users
                )
            }
        }
    }
}