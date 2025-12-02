package jik.imbc.videoplayer.trailer

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import jik.imbc.videoplayer.Thumbnail
import jik.imbc.videoplayer.icons.VideoPlayerIcons
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState.CAN_PLAY
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState.INITIAL
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun Trailer(
    modifier: Modifier = Modifier,
    thumbnailUrl: String,
    trailerUrl: String,
    viewModel: TrailerViewModel = viewModel()
) {
    var controllerVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var controllerVisibilityJob: Job? by remember { mutableStateOf(null) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.releasePlayer()
        }
    }

    Box(modifier = modifier.aspectRatio(500 / 281f)) {
        when (uiState.playerState) {
            INITIAL -> {
                Thumbnail(
                    imageUrl = thumbnailUrl,
                    start = { viewModel.start(url = trailerUrl) }
                )
            }

            else -> {
                TrailerPlayerView(
                    player = viewModel.player.player,
                    playOrPause = {
                        viewModel.playOrPause()
                        controllerVisible = true

                        controllerVisibilityJob?.cancel()
                        controllerVisibilityJob = coroutineScope.launch {
                            delay(1.3.seconds)
                            controllerVisible = false
                        }
                    }
                )
            }
        }

        val playbackIcon = if (uiState.playerState == TrailerPlayerState.PAUSED) {
            VideoPlayerIcons.Pause
        } else {
            VideoPlayerIcons.PlayArrow
        }

        when (uiState.playerState) {
            INITIAL -> Unit
            CAN_PLAY -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp),
                    color = Color.White,
                )
            }

            else -> TrailerController(
                playbackIcon = playbackIcon,
                visible = controllerVisible
            )
        }
    }
}

@Composable
private fun TrailerPlayerView(
    modifier: Modifier = Modifier,
    player: ExoPlayer,
    playOrPause: () -> Unit
) {
    Box(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = playOrPause
        )
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                    this.useController = false
                    this.layoutParams = ViewGroup.LayoutParams(
                        MATCH_PARENT,
                        MATCH_PARENT
                    )
                }
            },
        )
    }
}


@Composable
private fun BoxScope.TrailerController(
    modifier: Modifier = Modifier,
    visible: Boolean,
    playbackIcon: ImageVector,
) {

    AnimatedVisibility(
        modifier = modifier.align(Alignment.Center),
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = Color.Black.copy(alpha = 0.6f))
        ) {
            Icon(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .align(Alignment.Center),
                imageVector = playbackIcon,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}