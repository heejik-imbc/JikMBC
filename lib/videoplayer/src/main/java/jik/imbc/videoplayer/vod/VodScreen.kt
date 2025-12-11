package jik.imbc.videoplayer.vod

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import jik.imbc.ui.layout.noRippleClickable
import jik.imbc.videoplayer.R
import jik.imbc.videoplayer.icons.VideoPlayerIcons
import jik.imbc.videoplayer.player.vod.ActiveState
import jik.imbc.videoplayer.player.vod.VodPlayerState
import jik.imbc.videoplayer.player.vod.component.ControllerIcon
import jik.imbc.videoplayer.player.vod.component.controllerCenterIconSize
import kotlinx.coroutines.delay


@Composable
internal fun VodRoute(
    modifier: Modifier = Modifier,
    viewModel: VodViewModel = viewModel()
) {
    val uiState: VodUiState by viewModel.uiState.collectAsStateWithLifecycle()

    VodScreen(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black),
        player = viewModel.player.player,
        playerState = uiState.playerState,
        position = uiState.position,
        duration = uiState.duration,
        onBackward = viewModel::skipBack,
        onPlayPauseReplay = viewModel::playPauseReplay,
        onForward = viewModel::skipForward
    )
}

@Composable
private fun VodScreen(
    modifier: Modifier = Modifier,
    player: ExoPlayer,
    playerState: VodPlayerState,
    position: Long,
    duration: Long,
    onBackward: () -> Unit,
    onPlayPauseReplay: () -> Unit,
    onForward: () -> Unit,
) {

    var controllerVisible by remember { mutableStateOf(true) }

    LaunchedEffect(controllerVisible, playerState) {
        if (controllerVisible && playerState == VodPlayerState.PLAYING) {
            delay(3000)
            controllerVisible = false
        }
    }

    Box(modifier = modifier.noRippleClickable { controllerVisible = !controllerVisible }) {
        VodPlayer(player = player)

        when (playerState) {
            is VodPlayerState.INITIAL -> {}
            is VodPlayerState.ERROR -> {}
            is ActiveState -> {
                VodController(
                    visible = controllerVisible,
                    playerState = playerState,
                    position = position,
                    duration = duration,
                    onBackward = onBackward,
                    onPlayPauseReplay = onPlayPauseReplay,
                    onForward = onForward,
                    navigateUp = {}
                )
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun BoxScope.VodPlayer(
    modifier: Modifier = Modifier,
    player: ExoPlayer,
) {
    val presentationState = rememberPresentationState(player = player)
    val scaledModifier =
        modifier.resizeWithContentScale(
            contentScale = ContentScale.Fit,
            sourceSizeDp = presentationState.videoSizeDp
        )

    PlayerSurface(
        player = player,
        surfaceType = SURFACE_TYPE_SURFACE_VIEW,
        modifier = scaledModifier
    )


    if (presentationState.coverSurface) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black)
        )
    }
}

@Composable
private fun VodController(
    modifier: Modifier = Modifier,
    visible: Boolean,
    playerState: ActiveState,
    position: Long,
    duration: Long,
    onBackward: () -> Unit,
    onPlayPauseReplay: () -> Unit,
    onForward: () -> Unit,
    navigateUp: () -> Unit
) {

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(animationSpec = TweenSpec(durationMillis = 200)),
        exit = fadeOut(animationSpec = TweenSpec(durationMillis = 200)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            VodTopController(
                modifier = Modifier.weight(1f),
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
                duration = duration
            )
        }
    }
}

@Composable
private fun VodTopController(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit
) {

}


@Composable
private fun VodCenterController(
    modifier: Modifier = Modifier,
    state: ActiveState,
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
    state: ActiveState,
    onClick: () -> Unit
) {
    when (state) {
        VodPlayerState.BUFFERING ->
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(controllerCenterIconSize)
            )

        VodPlayerState.ENDED ->
            ControllerIcon(
                imageVector = VideoPlayerIcons.Replay,
                onClick = onClick
            )

        VodPlayerState.PAUSED ->
            ControllerIcon(
                imageVector = VideoPlayerIcons.PlayArrow,
                onClick = onClick
            )

        VodPlayerState.PLAYING ->
            ControllerIcon(
                imageVector = VideoPlayerIcons.Pause,
                onClick = onClick
            )
    }
}


@Composable
private fun VodBottomController(
    modifier: Modifier = Modifier,
    position: Long,
    duration: Long
) {

}

@Preview
@Composable
private fun VodScreenPreview() {
}