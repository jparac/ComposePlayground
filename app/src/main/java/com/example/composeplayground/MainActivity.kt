package com.example.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Screen()
            }
        }
    }
}

@Composable
fun Screen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(30) {
                Column(
                    modifier = Modifier.background(Color.Yellow)
                ) {
                    Divider()
                    Text(
                        text = "$it", modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePlaygroundTheme {
        Screen()
    }
}