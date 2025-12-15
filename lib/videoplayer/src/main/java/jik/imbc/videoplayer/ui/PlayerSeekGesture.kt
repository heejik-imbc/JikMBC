package jik.imbc.videoplayer.ui

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay

internal const val FADE_OUT_DURATION = 300
private const val INDICATOR_TIMEOUT = 800L
private const val CONSECUTIVE_TAP_TIMEOUT = 500L

enum class SeekDirection {
    FORWARD, BACKWARD, NONE
}

data class SeekState(
    val direction: SeekDirection = SeekDirection.NONE,
    val time: Long = 0,
    val visible: Boolean = false,
    val seekAmount: Long
) {
    fun updateBySeek(seekDirection: SeekDirection): SeekState {
        return SeekState(
            direction = seekDirection,
            time = if (this.direction == seekDirection) this.time + seekAmount else seekAmount,
            visible = true,
            seekAmount = seekAmount
        )
    }
}

@Composable
internal fun rememberSeekState(seekAmount: Long): MutableState<SeekState> {
    val state = remember {
        mutableStateOf(SeekState(seekAmount = seekAmount))
    }

    LaunchedEffect(seekAmount) {
        state.value = state.value.copy(seekAmount = seekAmount)
    }

    LaunchedEffect(key1 = state.value.time, key2 = state.value.direction) {
        if (state.value.time > 0) {
            delay(INDICATOR_TIMEOUT)
            state.value = state.value.copy(visible = false)

            delay(FADE_OUT_DURATION.toLong())
            state.value = state.value.copy(time = 0)
        }
    }

    return state
}

@Composable
internal fun Modifier.handleSeekTapGesture(
    seekState: MutableState<SeekState>,
    onSingleTap: () -> Unit,
    onLeftConsecutiveTap: () -> Unit,
    onRightConsecutiveTap: () -> Unit,
): Modifier {
    var lastTapTime by remember { mutableLongStateOf(0L) }
    var lastTapSide by remember { mutableStateOf(SeekDirection.NONE) }

    return this.pointerInput(Unit) {
        detectTapGestures(
            onTap = { offset ->
                val currentTime = System.currentTimeMillis()
                val isLeftSide = offset.x < size.width / 2
                val currentSide = if (isLeftSide) SeekDirection.BACKWARD else SeekDirection.FORWARD

                // 같은 방향의 연이은 탭인지 확인
                val isConsecutiveTap =
                    currentTime - lastTapTime <= CONSECUTIVE_TAP_TIMEOUT && lastTapSide == currentSide

                if (isConsecutiveTap) {
                    if (isLeftSide) {
                        onLeftConsecutiveTap()
                    } else {
                        onRightConsecutiveTap()
                    }

                    seekState.value = seekState.value.updateBySeek(currentSide)
                } else {
                    onSingleTap()
                }

                lastTapTime = currentTime
                lastTapSide = currentSide
            }
        )
    }
}