package com.example.composeplayground.ui.components.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun CanvasTouch(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PurpleBackgroundColor)
    ) {
        Spacer(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .align(Alignment.Center)
                .drawBehind {
                    val barWidthPx = 1.dp.toPx()
                    // draw box
                    drawRect(color = BarColor, style = Stroke(width = barWidthPx))

                    drawCircle(color = Color.Green, radius = 16.dp.toPx())
                }
        )
    }
}