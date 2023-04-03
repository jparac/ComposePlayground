package com.example.composeplayground.ui.components.canvas

import android.graphics.PointF
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CanvasBezier(
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
                .aspectRatio(3 / 2f)
                .fillMaxSize()
                .align(Alignment.Center)
                .drawBehind {
                    val barWidthPx = 1.dp.toPx()
                    // draw box
                    drawRect(color = BarColor, style = Stroke(width = barWidthPx))

                    val startPoint = PointF(size.width / 5, size.height * 4 / 5)
                    val endPoint = PointF(size.width * 4 / 5, size.height / 5)
                    val control1 = PointF(
                        (endPoint.x + startPoint.x) / 2f,
                        startPoint.y
                    )
                    val control2 = PointF(
                        (endPoint.x + startPoint.x) / 2f,
                        endPoint.y
                    )

                    drawCircle(color = Color.Green, radius = 8.dp.toPx(), center = Offset(endPoint.x, endPoint.y))
                    drawCircle(color = Color.Green, radius = 8.dp.toPx(), center = Offset(control2.x, control2.y), style = Stroke(2.dp.toPx()))
                    drawLine(color = Color.Green, start = Offset(control2.x, control2.y), end = Offset(endPoint.x, endPoint.y))
                    drawCircle(color = Color.Cyan, radius = 8.dp.toPx(), center = Offset(startPoint.x, startPoint.y))
                    drawCircle(color = Color.Cyan, radius = 8.dp.toPx(), center = Offset(control1.x, control1.y), style = Stroke(2.dp.toPx()))
                    drawLine(color = Color.Cyan, start = Offset(control1.x, control1.y), end = Offset(startPoint.x, startPoint.y))

                    val path2 = Path()
                    path2.moveTo(x = startPoint.x, y = startPoint.y)
                    path2.cubicTo(
                        x1 = control1.x,
                        y1 = control1.y,
                        x2 = control2.x,
                        y2 = control2.y,
                        x3 = endPoint.x,
                        y3 = endPoint.y
                    )
                    drawPath(color = Color.Red, path = path2, style = Stroke(1.dp.toPx()))
                }
        )
    }
}

