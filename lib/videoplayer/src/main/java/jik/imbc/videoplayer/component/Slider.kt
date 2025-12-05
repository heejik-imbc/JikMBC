package jik.imbc.videoplayer.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VPSlider(
    modifier: Modifier = Modifier,
    state: SliderState = rememberSliderState(),
    thumbColor: Color = MaterialTheme.colorScheme.primary,
    activeTrackColor: Color = MaterialTheme.colorScheme.primary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.primary
) {
    val interactionSource = remember { MutableInteractionSource() }

    Slider(
        modifier = modifier,
        state = state,
        interactionSource = interactionSource,
        thumb = {
            VPThumb(
                interactionSource = interactionSource,
                color = thumbColor
            )
        },
        track = {
            VPTrack(
                state = it,
                activeColor = activeTrackColor,
                inactiveColor = inactiveTrackColor
            )
        }
    )
}

@Composable
private fun VPThumb(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource,
    color: Color
) {
    val isDragged by interactionSource.collectIsDraggedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isDragged) 1.5f else 1f,
    )

    Spacer(
        modifier = modifier
            .padding(top = 2.dp)
            .size(size = 12.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(shape = CircleShape)
            .background(color = color)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VPTrack(
    modifier: Modifier = Modifier,
    state: SliderState,
    activeColor: Color,
    inactiveColor: Color
) {
    val coercedValueAsFraction = calcFraction(
        state.valueRange.start,
        state.valueRange.endInclusive,
        state.value
    )
    val height = 2.dp

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val sliderLeft = Offset(0f, center.y)
        val sliderRight = Offset(size.width, center.y)
        val sliderStart = if (isRtl) sliderRight else sliderLeft
        val sliderEnd = if (isRtl) sliderLeft else sliderRight
        val trackStrokeWidth = height.toPx()

        val sliderValueEnd = Offset(
            x = sliderEnd.x * coercedValueAsFraction,
            y = center.y
        )

        // inactive track
        drawLine(
            color = inactiveColor,
            start = sliderStart,
            end = sliderEnd,
            strokeWidth = trackStrokeWidth,
            cap = StrokeCap.Round
        )

        // active track
        drawLine(
            color = activeColor,
            start = sliderStart,
            end = sliderValueEnd,
            strokeWidth = trackStrokeWidth,
            cap = StrokeCap.Round
        )
    }
}

private fun calcFraction(start: Float, end: Float, currentValue: Float): Float =
    if (end == start) 0f
    else ((currentValue - start) / (end - start)).coerceIn(0f, 1f)


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun VPSliderPreview() {
    VPSlider()
}