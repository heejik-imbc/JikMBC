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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import jik.imbc.videoplayer.component.VPSlider
import jik.imbc.videoplayer.icons.VideoPlayerIcons
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun TrailerSection(
    modifier: Modifier = Modifier,
    thumbnailUrl: String,
    trailerUrl: String,
    autoPlay: Boolean = false,
    viewModel: TrailerViewModel = viewModel()
) {
    var controllerVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var controllerVisibilityJob: Job? by remember { mutableStateOf(null) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(key1 = Unit) {
        if (autoPlay) {
            viewModel.start(url = trailerUrl)
        }

        onDispose {
            viewModel.releasePlayer()
        }
    }

    Box(modifier = modifier.aspectRatio(500 / 281f)) {
        when (uiState.playerState) {
            TrailerPlayerState.INITIAL -> {
                TrailerThumbnail(
                    imageUrl = thumbnailUrl,
                    start = { viewModel.start(url = trailerUrl) }
                )
            }

            is TrailerPlayerState.ERROR -> Unit

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

        when (val state = uiState.playerState) {
            TrailerPlayerState.INITIAL, is TrailerPlayerState.ERROR -> Unit

            else -> {
                val playbackIcon = when (state) {
                    TrailerPlayerState.PAUSED -> VideoPlayerIcons.Pause
                    TrailerPlayerState.PLAYING -> VideoPlayerIcons.PlayArrow
                    TrailerPlayerState.ENDED -> VideoPlayerIcons.Replay
                    TrailerPlayerState.BUFFERING -> {
                        controllerVisibilityJob?.cancel()
                        controllerVisible = false
                        null
                    }

                    else -> throw IllegalStateException()
                }

                TrailerController(
                    playbackIcon = playbackIcon,
                    visible = controllerVisible,
                    position = uiState.position,
                    duration = uiState.duration,
                    changePosition = viewModel::changePosition
                )
            }
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
private fun TrailerController(
    modifier: Modifier = Modifier,
    visible: Boolean,
    playbackIcon: ImageVector?,
    position: Long,
    duration: Long,
    changePosition: (Long) -> Unit
) {

    Box(modifier = modifier.fillMaxSize()) {
        if (playbackIcon != null) {
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
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

        if (duration >= 0) {
            TrailerBottomController(
                modifier = Modifier.align(Alignment.BottomCenter),
                position = position,
                duration = duration,
                changePosition = changePosition
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.TrailerBottomController(
    modifier: Modifier = Modifier,
    position: Long,
    duration: Long,
    changePosition: (Long) -> Unit
) {
    val sliderState = rememberSliderState(
        valueRange = 0f..duration.coerceAtLeast(0).toFloat(),
    ).apply { onValueChange = { changePosition(it.toLong()) } }

    LaunchedEffect(key1 = position) {
        sliderState.value = position.toFloat()
    }

    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .offset(y = 3.dp)
                .height(3.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary
                )
        )
        VPSlider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 9.5.dp),
            state = sliderState
        )
    }
}