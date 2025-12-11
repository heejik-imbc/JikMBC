package jik.imbc.videoplayer.vod

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import jik.imbc.videoplayer.player.vod.ActiveState
import jik.imbc.videoplayer.player.vod.VodPlayerState


@Composable
internal fun VodRoute(
    modifier: Modifier = Modifier,
    viewModel: VodViewModel = viewModel()
) {
    val uiState: VodUiState by viewModel.uiState.collectAsStateWithLifecycle()

    VodScreen(
        modifier = modifier.background(color = Color.Black),
        player = viewModel.player.player,
        playerState = uiState.playerState,
        position = uiState.position,
        duration = uiState.duration
    )
}

@Composable
private fun VodScreen(
    modifier: Modifier = Modifier,
    player: ExoPlayer,
    playerState: VodPlayerState,
    position: Long,
    duration: Long
) {

    Box(modifier = modifier) {
        when (playerState) {
            is VodPlayerState.INITIAL -> {}
            is VodPlayerState.ERROR -> {}
            is ActiveState -> {
                VodPlayer(
                    player = player,
                    toggleControllerVisible = {}
                )
                VodController(
                    visible = true,
                    playerState = playerState,
                    position = position,
                    duration = duration,
                    navigateUp = {}
                )
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun VodPlayer(
    modifier: Modifier = Modifier,
    player: ExoPlayer,
    toggleControllerVisible: () -> Unit
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
        modifier = scaledModifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = {}
        )
    )
}

@Composable
private fun VodController(
    modifier: Modifier = Modifier,
    visible: Boolean,
    playerState: ActiveState,
    position: Long,
    duration: Long,
    navigateUp: () -> Unit
) {

    AnimatedVisibility(
        modifier = modifier,
        visible = visible
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
                state = playerState
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
    state: ActiveState
) {

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