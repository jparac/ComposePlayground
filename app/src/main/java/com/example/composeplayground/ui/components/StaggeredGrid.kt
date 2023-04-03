@file:OptIn(ExperimentalFoundationApi::class)

package com.example.composeplayground.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
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
fun StaggeredGrid() {
    val items = remember {
        val list = mutableListOf<Int>()
        repeat(20) {
            list.add((100..150).random())
        }
        mutableStateOf(list.toList())
    }
//    StaggeredVerticalGrid(items = items.value)
    StaggeredHorizontalGrid(items = items.value)
}

@Composable
fun CombinedStaggeredGrid() {
    val items = remember {
        val list = mutableListOf<Int>()
        repeat(20) {
            list.add((100..150).random())
        }
        mutableStateOf(list.toList())
    }
    LazyColumn {
        items(10) {
            Divider(thickness = 10.dp)
            LazyHorizontalStaggeredGrid(
                modifier = Modifier.height(100.dp),
                rows = StaggeredGridCells.Fixed(count = 2)
            ) {
                itemsIndexed(items.value) { index, item ->
                    Box(
                        modifier = Modifier
                            .width(item.dp)
                            .height(item.dp)
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
        }
    }
}

@Composable
private fun StaggeredVerticalGrid(items: List<Int>) {
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(count = 2)) {
        itemsIndexed(items) { index, item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(item.dp)
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
}

@Composable
private fun StaggeredHorizontalGrid(items: List<Int>) {
    LazyHorizontalStaggeredGrid(rows = StaggeredGridCells.Fixed(count = 2)) {
        itemsIndexed(items) { index, item ->
            Box(
                modifier = Modifier
                    .width(item.dp)
                    .height(50.dp)
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
}

