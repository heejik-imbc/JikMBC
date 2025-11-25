package jik.imbc.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.delay

@Composable
fun EffectColumn(
    modifier: Modifier = Modifier,
    delayPerItem: Long = 300,
    content: @Composable () -> Unit
) {

    var visibleCount by remember { mutableIntStateOf(0) }
    var maxCount = remember { 0 }

    LaunchedEffect(key1 = maxCount) {
        while (visibleCount < maxCount) {
            delay(delayPerItem)
            visibleCount++
        }
    }

    val measurePolicy: MeasurePolicy = remember {
        object : MeasurePolicy {
            override fun MeasureScope.measure(
                measurables: List<Measurable>,
                constraints: Constraints
            ): MeasureResult {

                val placeables = measurables.map { it.measure(constraints) }
                if (maxCount == 0) {
                    maxCount = placeables.size
                }

                val totalHeight = placeables.sumOf { it.height }

                return layout(constraints.maxWidth, totalHeight) {
                    var y = 0
                    for (i in 0 until visibleCount) {
                        val placeable = placeables[i]
                        placeable.placeRelative(x = 0, y = y)
                        y += placeable.height
                    }
                }
            }
        }
    }

    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = measurePolicy
    )
}