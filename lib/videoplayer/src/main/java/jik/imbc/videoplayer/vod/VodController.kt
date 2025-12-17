package jik.imbc.videoplayer.vod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jik.imbc.designsystem.icon.JbcIcons.ArrowBack
import jik.imbc.ui.layout.noRippleClickable
import jik.imbc.videoplayer.R
import jik.imbc.videoplayer.component.VPSlider
import jik.imbc.videoplayer.icons.VideoPlayerIcons
import jik.imbc.videoplayer.pip.enterPip
import jik.imbc.videoplayer.player.vod.VodPlayerState
import jik.imbc.videoplayer.player.vod.component.ControllerIcon
import jik.imbc.videoplayer.player.vod.component.controllerCenterIconSize
import jik.imbc.videoplayer.ui.SeekDirection
import jik.imbc.videoplayer.ui.SeekState

@Composable
internal fun VodController(
    modifier: Modifier = Modifier,
    visible: Boolean,
    playerState: VodPlayerState.ActiveState,
    title: String,
    position: Long,
    duration: Long,
    seekAmount: Long,
    onBackward: () -> Unit,
    onPlayPauseReplay: () -> Unit,
    onForward: () -> Unit,
    changePosition: (Long) -> Unit,
    changeSeekAmount: () -> Unit,
    navigateUp: () -> Unit
) {

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(animationSpec = TweenSpec(durationMillis = 200)),
        exit = fadeOut(animationSpec = TweenSpec(durationMillis = 200)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            VodTopController(
                modifier = Modifier.weight(1f),
                title = title,
                navigateUp = navigateUp
            )
            VodCenterController(
                modifier = Modifier.weight(1f),
                state = playerState,
                onBackward = onBackward,
                onPlayPauseReplay = onPlayPauseReplay,
                onForward = onForward
            )
            VodBottomController(
                modifier = Modifier.weight(1f),
                position = position,
                duration = duration,
                seekAmount = seekAmount,
                changePosition = changePosition,
                changeSeekAmount = changeSeekAmount
            )
        }
    }
}

@Composable
private fun VodTopController(
    modifier: Modifier = Modifier,
    title: String,
    navigateUp: () -> Unit,
) {

    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 24.dp, end = 24.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopStart)
                .noRippleClickable(onClick = navigateUp),
            imageVector = ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
        )
        Text(
            modifier = Modifier.align(Alignment.TopCenter),
            text = title,
            color = Color.White,
            fontSize = 14.sp
        )

        Icon(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopEnd)
                .noRippleClickable(onClick = { context.enterPip() }),
            imageVector = VideoPlayerIcons.Pip,
            contentDescription = "Picture in Picture",
            tint = Color.White,
        )
    }
}


@Composable
private fun VodCenterController(
    modifier: Modifier = Modifier,
    state: VodPlayerState.ActiveState,
    onBackward: () -> Unit,
    onPlayPauseReplay: () -> Unit,
    onForward: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        ControllerIcon(
            painterResourceId = R.drawable.media3_icon_skip_back,
            onClick = onBackward
        )
        Spacer(modifier = Modifier.weight(0.2f))
        CenterPlaybackControl(
            state = state,
            onClick = onPlayPauseReplay
        )
        Spacer(modifier = Modifier.weight(0.2f))
        ControllerIcon(
            painterResourceId = R.drawable.media3_icon_skip_forward,
            onClick = onForward
        )
        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@Composable
private fun CenterPlaybackControl(
    state: VodPlayerState.ActiveState,
    onClick: () -> Unit
) {
    when (state) {
        VodPlayerState.Buffering ->
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(controllerCenterIconSize)
            )

        VodPlayerState.Ended ->
            ControllerIcon(
                imageVector = VideoPlayerIcons.Replay,
                onClick = onClick
            )

        VodPlayerState.Paused ->
            ControllerIcon(
                imageVector = VideoPlayerIcons.PlayArrow,
                onClick = onClick
            )

        VodPlayerState.Playing ->
            ControllerIcon(
                imageVector = VideoPlayerIcons.Pause,
                onClick = onClick
            )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VodBottomController(
    modifier: Modifier = Modifier,
    position: Long,
    duration: Long,
    seekAmount: Long,
    changePosition: (Long) -> Unit,
    changeSeekAmount: () -> Unit
) {
    val sliderState = rememberSliderState(
        valueRange = 0f..duration.coerceAtLeast(0).toFloat(),
    ).apply { onValueChange = { changePosition(it.toLong()) } }

    LaunchedEffect(key1 = position) {
        sliderState.value = position.toFloat()
    }

    Column(modifier = modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.Center) {
        VPSlider(
            state = sliderState,
            thumbSize = 18.dp,
            trackHeight = 5.dp
        )
        VodBottomExtraController(
            seekAmount = seekAmount,
            changeSeekAmount = changeSeekAmount
        )
    }
}

@Composable
private fun VodBottomExtraController(
    modifier: Modifier = Modifier,
    seekAmount: Long,
    changeSeekAmount: () -> Unit
) {
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(size = 20.dp))
                .background(color = Color.Black.copy(alpha = 0.6f))
                .clickable(onClick = changeSeekAmount)
                .animateContentSize(),
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 12.dp)
                    .align(Alignment.Center),
                text = "Skip Interval: ${seekAmount / 1000}s",
                color = Color.White
            )
        }
    }
}

@Composable
internal fun SeekIndicator(
    modifier: Modifier = Modifier,
    seekState: SeekState
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        SeekIndicatorItem(
            modifier = Modifier.alpha(if (seekState.direction == SeekDirection.BACKWARD) 1f else 0f),
            direction = SeekDirection.BACKWARD,
            time = seekState.time
        )
        Spacer(modifier = Modifier.weight(0.6f))
        SeekIndicatorItem(
            modifier = Modifier.alpha(if (seekState.direction == SeekDirection.FORWARD) 1f else 0f),
            direction = SeekDirection.FORWARD,
            time = seekState.time
        )
        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@Composable
private fun SeekIndicatorItem(
    modifier: Modifier = Modifier,
    direction: SeekDirection,
    time: Long,
) {

    val suffix = if (direction == SeekDirection.FORWARD) "+" else "-"

    Text(
        modifier = modifier,
        text = "$suffix${time / 1000}",
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
    )
}