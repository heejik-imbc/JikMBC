package jik.imbc.videoplayer.vod

import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import jik.imbc.videoplayer.pip.SetPipForPreAndroid12
import jik.imbc.videoplayer.pip.setPipForPostAndroid12
import jik.imbc.videoplayer.player.vod.VodPlayerState
import jik.imbc.videoplayer.ui.FADE_OUT_DURATION
import jik.imbc.videoplayer.ui.SeekDirection
import jik.imbc.videoplayer.ui.handleSeekTapGesture
import jik.imbc.videoplayer.ui.rememberSeekState
import kotlinx.coroutines.delay


@Composable
fun VodRoute(
    modifier: Modifier = Modifier,
    viewModel: VodViewModel
) {
    val uiState: VodUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current

    VodScreen(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black),
        player = viewModel.player.player,
        playerState = uiState.playerState,
        title = uiState.content.title,
        position = uiState.position,
        duration = uiState.duration,
        seekAmount = uiState.seekAmount,
        onBackward = viewModel::skipBack,
        onPlayPauseReplay = viewModel::playPauseReplay,
        onForward = viewModel::skipForward,
        changePosition = viewModel::changePosition,
        changeSeekAmount = viewModel::changeSeekAmount,
        navigateUp = { activity?.finish() }
    )
}

@Composable
private fun VodScreen(
    modifier: Modifier = Modifier,
    player: ExoPlayer,
    playerState: VodPlayerState,
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

    var controllerVisible by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = controllerVisible, key2 = playerState) {
        if (controllerVisible && playerState == VodPlayerState.Playing) {
            delay(3000)
            controllerVisible = false
        }
    }

    val seekState = rememberSeekState(seekAmount = seekAmount)

    val playerStateUpdated by rememberUpdatedState(newValue = playerState)
    var prePlayerState by remember { mutableStateOf(playerState) }
    var preControllerVisible by remember { mutableStateOf(controllerVisible) }

    Box(
        modifier = modifier
            .handleSeekTapGesture(
                seekState = seekState,
                onSingleTap = {
                    preControllerVisible = controllerVisible
                    prePlayerState = playerStateUpdated

                    controllerVisible = !controllerVisible
                },
                onLeftConsecutiveTap = {
                    onBackward()
                    controllerVisible = preControllerVisible || prePlayerState !is VodPlayerState.Playing
                },
                onRightConsecutiveTap = {
                    onForward()
                    controllerVisible = preControllerVisible || prePlayerState !is VodPlayerState.Playing
                },
            )
    ) {
        VodPlayer(player = player)

        when (playerState) {
            is VodPlayerState.Initial -> {}
            is VodPlayerState.Error -> {}
            is VodPlayerState.ActiveState -> {
                VodController(
                    visible = controllerVisible,
                    playerState = playerState,
                    title = title,
                    position = position,
                    duration = duration,
                    seekAmount = seekAmount,
                    onBackward = {
                        seekState.value = seekState.value.updateBySeek(SeekDirection.BACKWARD)
                        onBackward()
                    },
                    onPlayPauseReplay = onPlayPauseReplay,
                    onForward = {
                        seekState.value = seekState.value.updateBySeek(SeekDirection.FORWARD)
                        onForward()
                    },
                    navigateUp = navigateUp,
                    changePosition = changePosition,
                    changeSeekAmount = changeSeekAmount
                )

                AnimatedVisibility(
                    visible = seekState.value.visible,
                    enter = fadeIn(TweenSpec(durationMillis = FADE_OUT_DURATION)),
                    exit = fadeOut(TweenSpec(durationMillis = FADE_OUT_DURATION)),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    SeekIndicator(seekState = seekState.value)
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun BoxScope.VodPlayer(
    modifier: Modifier = Modifier,
    player: ExoPlayer,
    shouldEnterPipMode: Boolean = true
) {
    val presentationState = rememberPresentationState(player = player)
    val scaledModifier =
        modifier.resizeWithContentScale(
            contentScale = ContentScale.Fit,
            sourceSizeDp = presentationState.videoSizeDp
        )

    SetPipForPreAndroid12(shouldEnterPipMode = shouldEnterPipMode)

    PlayerSurface(
        modifier = scaledModifier.setPipForPostAndroid12(player, shouldEnterPipMode = shouldEnterPipMode),
        player = player,
        surfaceType = SURFACE_TYPE_SURFACE_VIEW
    )

    if (presentationState.coverSurface) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black)
        )
    }
}

@Preview
@Composable
private fun VodScreenPreview() {
}