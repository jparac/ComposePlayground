package com.example.composeplayground.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
private fun DynamicWidthLayout(
    modifier: Modifier = Modifier,
    mainContent: @Composable () -> Unit,
    dependentContent: @Composable (IntSize) -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        var mainPlaceables: List<Placeable> = subcompose(SlotsEnum.Main, mainContent).map {
            it.measure(constraints)
        }
        var maxSize =
            mainPlaceables.fold(IntSize.Zero) { currentMax: IntSize, placeable: Placeable ->
                IntSize(
                    width = maxOf(currentMax.width, placeable.width),
                    height = maxOf(currentMax.height, placeable.height)
                )
            }
        val dependentMeasurables: List<Measurable> = subcompose(SlotsEnum.Dependent) {
            // Send maxSize of mainComponent to
            // dependent composable in case it might be used
            dependentContent(maxSize)
        }
        val dependentPlaceables: List<Placeable> = dependentMeasurables
            .map { measurable: Measurable ->
                measurable.measure(Constraints(maxSize.width, constraints.maxWidth))
            }

        // Get maximum width of dependent composable
        val maxWidth = dependentPlaceables.maxOf { it.width }

        // If width of dependent composable is longer than main one, remeasure main one
        // with dependent composable's width using it as minimumWidthConstraint
        if (maxWidth > maxSize.width) {
            mainPlaceables = subcompose(2, mainContent).map {
                it.measure(Constraints(maxWidth, constraints.maxWidth))
            }
        }

        // Our final maxSize is longest width and total height of main and dependent composables
        maxSize = IntSize(
            maxSize.width.coerceAtLeast(maxWidth),
            maxSize.height + dependentPlaceables.maxOf { it.height }
        )

        layout(maxSize.width, maxSize.height) {
            // Place layouts
            mainPlaceables.forEach { it.placeRelative(0, 0) }
            dependentPlaceables.forEach {
                it.placeRelative(0, mainPlaceables.maxOf { it.height })
            }
        }
    }
}


enum class SlotsEnum { Main, Dependent }



@Composable
fun TutorialContent() {
    val density = LocalDensity.current.density
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var mainText by remember { mutableStateOf(TextFieldValue("Main Component")) }
        var dependentText by remember { mutableStateOf(TextFieldValue("Dependent Component")) }

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            value = mainText,
            label = { Text("Main") },
            placeholder = { Text("Set text to change main width") },
            onValueChange = { newValue: TextFieldValue ->
                mainText = newValue
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            value = dependentText,
            label = { Text("Dependent") },
            placeholder = { Text("Set text to change dependent width") },
            onValueChange = { newValue ->
                dependentText = newValue
            }
        )

        DynamicWidthLayout(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.LightGray)
                .padding(8.dp),
            mainContent = {
                Column(
                    modifier = Modifier
                        .background(Color.Red)
                        .padding(4.dp)
                ) {
                    Text(
                        text = mainText.text,
                        modifier = Modifier
                            .background(Color.Blue)
                            .height(40.dp),
                        color = Color.White
                    )
                }
            },
            dependentContent = { size: IntSize ->
                // Measure max width of main component in dp  retrieved
                // by subCompose of dependent component from IntSize
                val maxWidth = with(density) {
                    size.width / this
                }.dp
                Column(
                    modifier = Modifier
                        .background(Color.Green)
                        .padding(4.dp)
                ) {

                    Text(
                        text = dependentText.text,
                        modifier = Modifier
                            .background(Color.Blue),
                        color = Color.White
                    )
                }
            }
        )
    }
}