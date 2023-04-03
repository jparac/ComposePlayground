package com.example.composeplayground.ui.components.canvas

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private fun Float.toRad(): Float {
    return Math.toRadians(this.toDouble()).toFloat()
}

/**
 * https://medium.com/@alexruskovski/jetpack-compose-custom-views-c5fe3d6cbb03
 */
@Composable
fun MusicKnob(
    modifier: Modifier = Modifier,
    numberOfDots: Int = 40,
    onVolumeChanged: (Float) -> Unit = {}
) {
    var state by remember { mutableStateOf(false) }
    val colourTransitionDurationMs = 1000

    var angle by remember { mutableStateOf(0f) }
    var dragStartedAngle by remember { mutableStateOf(0f) }
    var oldAngle by remember { mutableStateOf(angle) }
    var volume by remember { mutableStateOf(0f) }

    val ringColorOff = Color.LightGray
    val ringColorON = Color.Green
    val offsetAngleDegree = 20f

    val ringColor by animateColorAsState(
        targetValue = if (state) ringColorON else ringColorOff,
        animationSpec = tween(
            durationMillis = colourTransitionDurationMs,
        )
    )


    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStartedAngle = atan2(
                            y = size.center.x - offset.x,
                            x = size.center.y - offset.y
                        ) * (180f / Math.PI.toFloat()) * -1
                    },
                    onDragEnd = {
                        oldAngle = angle
                    }
                ) { change, _ ->

                    val touchAngle = atan2(
                        y = size.center.x - change.position.x,
                        x = size.center.y - change.position.y
                    ) * (180f / Math.PI.toFloat()) * -1

                    angle = oldAngle + (touchAngle - dragStartedAngle)

                    //we want to work with positive angles
                    if (angle > 360) {
                        angle -= 360
                    } else if (angle < 0) {
                        angle = 360 - abs(angle)
                    }

                    if (angle > 360f - (offsetAngleDegree * .8f)) {
                        angle = 0f
                    } else if (angle > 0f && angle < offsetAngleDegree) {
                        angle = offsetAngleDegree
                    }
                    //determinants the state of the nob. OFF or ON
                    state = angle >= offsetAngleDegree && angle <= (360f - offsetAngleDegree / 2)

                    val newVolume = if (angle < offsetAngleDegree) {
                        0f
                    } else {
                        (angle) / (360f - offsetAngleDegree)
                    }

                    volume = newVolume.coerceIn(
                        minimumValue = 0f,
                        maximumValue = 1f
                    )

                    onVolumeChanged(newVolume)
                }
            }
    ) {
        val halfScreenWidth = size.width * 0.5f
        val radius = halfScreenWidth - (halfScreenWidth * 0.25f)

        circle(
            ringColor = ringColor,
            radius = radius
        )

        dots(
            numberOfDots = numberOfDots,
            offsetAngleDegree = offsetAngleDegree,
            radius = radius,
            angle = angle
        )

        onOff(
            state = state,
            radius = radius,
            volume = volume
        )

        val spacing = radius * 0.225f
        val knobCenter = Offset(
            x = (radius - spacing) * cos((angle - 90f).toRad()) + size.center.x,
            y = (radius - spacing) * sin((angle - 90f).toRad()) + size.center.y
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, Color.DarkGray),
                center = knobCenter,
                radius = radius * (1f / 9f)
            ),
            radius = 20.dp.toPx(),
            center = knobCenter,
        )
    }
}

private fun DrawScope.circle(
    ringColor: Color,
    radius: Float
) {
    drawCircle(
        color = ringColor,
        radius = radius,
        center = size.center,
        style = Stroke(
            width = 16.dp.toPx()
        ),
    )
}

private fun DrawScope.dots(
    numberOfDots: Int = 40,
    offsetAngleDegree: Float = 20f,
    radius: Float,
    angle: Float
) {
    val dotCircleNormalColor = Color(0xff00ff00)
    val dotCircleEmphasisedColor = Color(0xff9737bf)

    val lineDegree = (360f - offsetAngleDegree * 2) / numberOfDots

    for (dotNumber in 0..numberOfDots) {
        val angleInDegrees = lineDegree * dotNumber - 90f + offsetAngleDegree
        val angleRad = Math.toRadians(angleInDegrees.toDouble()).toFloat()
        val dotDistanceFromMainCircle = radius * 0.15f

        val isDotEmphasised =
            angle >= angleInDegrees + 90f && angle < (360f - offsetAngleDegree / 2)

        val normalDotRad = radius * .015f
        val emphasisedDotRad = normalDotRad + (dotNumber * (radius * .001f))

        val dotRadius = if (isDotEmphasised) {
            emphasisedDotRad
        } else {
            normalDotRad
        }

        val dotColor = if (isDotEmphasised) {
            dotCircleEmphasisedColor
        } else {
            dotCircleNormalColor
        }

        drawCircle(
            color = dotColor,
            center = Offset(
                x = (radius + dotDistanceFromMainCircle) * cos(angleRad) + size.center.x,
                y = (radius + dotDistanceFromMainCircle) * sin(angleRad) + size.center.y,
            ),
            radius = dotRadius
        )
    }
}

private fun DrawScope.onOff(
    state: Boolean,
    radius: Float,
    volume: Float
) {
    drawContext.canvas.nativeCanvas.apply {
        val text = if (state) "ON" else "OFF"
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            textSize = radius * 0.15f
            color = Color.DarkGray.hashCode()

        }
        val textRect = Rect()
        paint.getTextBounds(text, 0, text.length, textRect)

        val positionX = size.center.x - (textRect.width() / 2)
        val positionY = size.center.y - radius - (0.15f * radius) + (textRect.height() / 2)

        drawText(
            text,
            positionX,
            positionY,
            paint
        )

        val volumeFormatter = DecimalFormat("#.#")

        if (state) {
            val volumeText = if (state) {
                volumeFormatter.format((volume * 100))
            } else "OFF"

            val volumePaint = Paint()
            volumePaint.color = android.graphics.Color.parseColor("#9737bf")
            volumePaint.textSize = (radius / 2)
            val volumeTextRect = Rect()
            volumePaint.getTextBounds(volumeText, 0, volumeText.length, volumeTextRect)

            val volumeTextPositionX = size.center.x - (volumeTextRect.width() / 2)
            val volumeTextPositionY = size.center.y + (volumeTextRect.height() / 2)

            drawText(
                volumeText,
                volumeTextPositionX,
                volumeTextPositionY,
                volumePaint
            )
        }
    }
}