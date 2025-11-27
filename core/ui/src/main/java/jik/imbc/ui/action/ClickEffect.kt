package jik.imbc.ui.action

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.onClickWithPressEffect(
    scaleFactor: Float = 0.8f,
    alphaFactor: Float = 1f,
    pressColor: Color = Color.Transparent,
    onClick: () -> Unit
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) scaleFactor else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isPressed) alphaFactor else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    val animatedPressColor by animateColorAsState(
        if (isPressed) pressColor else Color.Transparent,
    )

    this
        .scale(animatedScale)
        .alpha(animatedAlpha)
        .background(animatedPressColor)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isPressed = true
                    val released = tryAwaitRelease()
                    isPressed = false
                    if (released) onClick.invoke()
                },
            )
        }
}