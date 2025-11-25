package jik.imbc.ui.layout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
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
import kotlinx.coroutines.launch

private const val ITEM_ANIMATION_OFFSET_Y_PIXEL = 100

@Composable
fun EffectColumn(
    modifier: Modifier = Modifier,
    delayPerItem: Long = 200,
    content: @Composable () -> Unit
) {
    var visibleCount by remember { mutableIntStateOf(0) }
    var maxCount = remember { 0 }
    val animations = remember { mutableListOf<Animatable<Float, AnimationVector1D>>() }

    LaunchedEffect(Unit) {
        while (animations.size < maxCount) {
            animations.add(Animatable(0f))
        }

        repeat(maxCount) { index ->
            visibleCount++
            launch {
                animations[index].animateTo(1f, tween(durationMillis = 200))
            }

            delay(delayPerItem)
        }
    }

    val measurePolicy = remember {
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
                    placeables.forEachIndexed { index, placeable ->
                        val progress = animations.getOrNull(index)?.value ?: 1f
                        val offsetY = ((1f - progress) * ITEM_ANIMATION_OFFSET_Y_PIXEL).toInt()

                        if (index < visibleCount) {
                            placeable.placeRelative(x = 0, y = y + offsetY)
                        }
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