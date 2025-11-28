package jik.imbc.videoplayer.trailer

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import jik.imbc.videoplayer.Thumbnail
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState

@Composable
fun Trailer(
    modifier: Modifier = Modifier,
    thumbnailUrl: String,
    trailerUrl: String,
    viewModel: TrailerViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.releasePlayer()
        }
    }

    Box(modifier = modifier.aspectRatio(500 / 281f)) {
        when (uiState.playerState) {
            TrailerPlayerState.INITIAL -> {
                Thumbnail(
                    imageUrl = thumbnailUrl,
                    start = { viewModel.start(url = trailerUrl) }
                )
            }

            TrailerPlayerState.PLAYING, TrailerPlayerState.PAUSED -> {
                TrailerPlayerView(
                    player = viewModel.player.player,
                    playOrPause = viewModel::playOrPause
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