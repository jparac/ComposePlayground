package com.example.composeplayground.ui.components.canvas

import android.graphics.PointF
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun CanvasGraph(
    modifier: Modifier = Modifier
) {
    val animationProgress = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = graphData) {
        animationProgress.animateTo(targetValue = 1f, animationSpec = tween(3000))
    }
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
                    val path = generateSmoothPath(
                        data = graphData,
                        size = size,
                        animationProgress = animationProgress.value
                    )
                    val filledPath = Path()
                    filledPath.addPath(path)
                    filledPath.lineTo(x = size.width, y = size.height)
                    filledPath.lineTo(x = 0f, y = size.height)
                    filledPath.close()

                    val barWidthPx = 1.dp.toPx()
                    // draw box
                    drawRect(color = BarColor, style = Stroke(width = barWidthPx))

                    // draw vertical lines
                    val verticalLines = 4
                    val verticalSize = size.width / (verticalLines + 1)
                    repeat(verticalLines) { i ->
                        val startX = verticalSize * (i + 1)
                        drawLine(
                            color = BarColor,
                            start = Offset(x = startX, y = 0f),
                            end = Offset(x = startX, y = size.height),
                            strokeWidth = barWidthPx
                        )
                    }

                    // draw horizontal lines
                    val horizontalLines = 3
                    val horizontalSize = size.height / (horizontalLines + 1)
                    repeat(horizontalLines) { i ->
                        val startY = horizontalSize * (i + 1)
                        drawLine(
                            color = BarColor,
                            start = Offset(x = 0f, y = startY),
                            end = Offset(x = size.width, y = startY),
                            strokeWidth = barWidthPx
                        )
                    }
                    clipRect(right = size.width * animationProgress.value) {
                        // draw path
                        drawPath(
                            path = path,
                            color = Color.Green,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // gradient bg
                        val brush = Brush.verticalGradient(
                            colors = listOf(Color.Green.copy(alpha = 0.4f), Color.Transparent)
                        )
                        drawPath(path = filledPath, brush = brush, style = Fill)
                    }
                }
        )
    }
}

private fun generatePath(data: List<Balance>, size: Size): Path {
    val path = Path()
    val numberEntries = data.size - 1
    val weekWidth = size.width / numberEntries

    val max = data.maxBy { it.amount }
    val min = data.minBy { it.amount } // will map to x= 0, y = height
    val range = max.amount - min.amount
    val heightPxPerAmount = size.height / range.toFloat()

    data.forEachIndexed { i, balance ->
        if (i == 0) {
            path.moveTo(
                x = 0f,
                y = size.height - (balance.amount - min.amount).toFloat() * heightPxPerAmount
            )
        }
        val balanceX = i * weekWidth
        val balanceY = size.height - (balance.amount - min.amount).toFloat() * heightPxPerAmount
        path.lineTo(balanceX, balanceY)
    }
    return path
}

fun generateSmoothPath(data: List<Balance>, size: Size, animationProgress: Float): Path {
    val path = Path()
    val numberEntries = data.size - 1
    val weekWidth = size.width / numberEntries

    val max = data.maxBy { it.amount }
    val min = data.minBy { it.amount } // will map to x= 0, y = height
    val range = max.amount - min.amount
    val heightPxPerAmount = size.height / range.toFloat()

    var previousBalanceX = 0f
    var previousBalanceY = size.height
    data.forEachIndexed { i, balance ->
        if (i == 0) {
            path.moveTo(
                x = 0f,
                y = size.height - (balance.amount - min.amount).toFloat() * heightPxPerAmount * animationProgress
            )
        }

        val balanceX = i * weekWidth
        val balanceY =
            size.height - (balance.amount - min.amount).toFloat() * heightPxPerAmount * animationProgress
        // to do smooth curve graph - we use cubicTo, uncomment section below for non-curve
        val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
        val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)
        path.cubicTo(
            x1 = controlPoint1.x,
            y1 = controlPoint1.y,
            x2 = controlPoint2.x,
            y2 = controlPoint2.y,
            x3 = balanceX,
            y3 = balanceY
        )

        previousBalanceX = balanceX
        previousBalanceY = balanceY
    }
    return path
}

// date + balance
// list of date + balance
val graphData = listOf(
    Balance(LocalDate.now(), BigDecimal(65631)),
    Balance(LocalDate.now().plusWeeks(1), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(2), BigDecimal(65851)),
    Balance(LocalDate.now().plusWeeks(3), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(4), BigDecimal(66484)),
    Balance(LocalDate.now().plusWeeks(5), BigDecimal(67684)),
    Balance(LocalDate.now().plusWeeks(6), BigDecimal(66684)),
    Balance(LocalDate.now().plusWeeks(7), BigDecimal(66984)),
    Balance(LocalDate.now().plusWeeks(8), BigDecimal(70600)),
    Balance(LocalDate.now().plusWeeks(9), BigDecimal(71600)),
    Balance(LocalDate.now().plusWeeks(10), BigDecimal(72600)),
    Balance(LocalDate.now().plusWeeks(11), BigDecimal(72526)),
    Balance(LocalDate.now().plusWeeks(12), BigDecimal(72976)),
    Balance(LocalDate.now().plusWeeks(13), BigDecimal(73589)),
)

data class Balance(val date: LocalDate, val amount: BigDecimal)

val PurpleBackgroundColor = Color(0xff322049)
val BarColor = Color.White.copy(alpha = 0.3f)