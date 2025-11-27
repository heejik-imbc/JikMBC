package jik.imbc.ui.count

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AnimatedCounter(
    start: Int = 0,
    target: Int,
    durationMillis: Int = 800,
    content: @Composable (String) -> Unit
) {
    var targetValue by remember { mutableIntStateOf(start) }

    val animatedRatingCount by animateIntAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = durationMillis)
    )

    LaunchedEffect(key1 = Unit) {
        targetValue = target
    }

    content(animatedRatingCount.toString())
}