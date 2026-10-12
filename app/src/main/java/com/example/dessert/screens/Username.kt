package com.example.dessert.screens

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dessert.R

@Preview(showBackground = true)
@Composable
fun UsernamePage(value: String = "", onValueChange: (String) -> Unit = {}, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 60.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Player Name",
            fontSize = 38.sp,
            lineHeight = 36.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight(700)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = { Text("Input your name") },
            singleLine = true
        )
        Button(
            onClick = { onClick() },
        ) {
            Text(
                text = "Start",
                fontSize = 38.sp,
                lineHeight = 36.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(700)
            )
        }
    }
}

