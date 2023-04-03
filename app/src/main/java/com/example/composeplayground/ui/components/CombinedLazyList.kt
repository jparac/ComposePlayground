package com.example.composeplayground.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CombinedLazyList() {
    LazyColumn {
        items(20) {
            val items = remember {
                val list = mutableListOf<Int>()
                repeat(20) {
                    list.add(it)
                }
                mutableStateOf(list)
            }
            LazyRow(contentPadding = PaddingValues(8.dp)) {
                itemsIndexed(items.value) { item: Int, i: Int->
                    Box(modifier = Modifier
                        .size(150.dp)
                        .background(Color.Gray)
                        .border(width = 2.dp, color = Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.toString(),
                            textAlign = TextAlign.Center,
                            fontSize = 40.sp,
                        )
                    }
                }
            }
            Divider(thickness = 4.dp)
        }
    }
}