package com.example.composeplayground.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.ResistanceConfig
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DraggableButton() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val redSize = 56.dp

        val swipeableState = rememberSwipeableState(0)
        val sizePx = with(LocalDensity.current) {
            maxWidth.toPx() - redSize.toPx() - 20.dp.toPx()
        }
        val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

        Box(
            modifier = Modifier
                .testTag("SWIPE")
                .fillMaxWidth()
                .padding(10.dp)
                .clipToBounds()
                .clip(RoundedCornerShape(percent = 50))
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.9f) },
                    orientation = Orientation.Horizontal,
                    resistance = ResistanceConfig(
                        basis = 1f,
                        factorAtMin = SwipeableDefaults.StiffResistanceFactor,
                        factorAtMax = SwipeableDefaults.StiffResistanceFactor
                    )
                )

        ) {
            Box(
                Modifier
                    .testTag("RED")
                    .offset {
                        IntOffset(
                            x = swipeableState.offset.value.roundToInt(),
                            y = 0
                        )
                    }
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(percent = 50)
                    )
                    .clip(RoundedCornerShape(percent = 50))
                    .height(redSize)
                    .fillMaxWidth()
                    .background(Color.Red)
            )
            Log.d("jozo", "swipe state: ${swipeableState.progress}")
            val progress = swipeableState.progress
            val alpha = if (progress.from == progress.to && progress.from == 0) {
                1f
            } else if (progress.from != progress.to && progress.from == 1) {
                0f
            } else {
                1.0f - progress.fraction
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .alpha(alpha),
                text = "swipe to pay",
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Icon(
                modifier = Modifier
                    .testTag("CIRCLE")
                    .offset {
                        IntOffset(
                            x = swipeableState.offset.value.roundToInt(),
                            y = 0
                        )
                    }
                    .size(56.dp)
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .shadow(elevation = 5.dp, shape = CircleShape)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(Color.White),
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = "",
                tint = Color.Red
            )
        }
    }
}
