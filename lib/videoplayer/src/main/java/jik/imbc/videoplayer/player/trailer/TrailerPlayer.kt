package jik.imbc.videoplayer.player.trailer

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState.Companion.positionValidStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class TrailerPlayer(context: Context) {

    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    val state: MutableStateFlow<TrailerPlayerState> = MutableStateFlow(TrailerPlayerState.INITIAL)

    val duration = MutableStateFlow(C.TIME_UNSET)

    val currentPosition = flow {
        while (true) {
            if (state.value in positionValidStates) {
                emit(player.currentPosition)
            }
            delay(1.seconds / 30)
        }
    }

    init {
        initialize()
    }

    fun initialize() {
        player.apply {
            addListener(trailerPlayerListener {
                state.value = it

                this@TrailerPlayer.duration.let { duration ->
                    if (duration.value == C.TIME_UNSET) {
                        duration.value = player.duration
                    }
                }
            })
        }
    }

    fun release() {
        player.release()
    }

    fun start(url: String) {
        player.apply {
            setMediaItem(MediaItem.fromUri(url))
            playWhenReady = true
            prepare()
        }
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun changePosition(position: Long) {
        player.seekTo(position)
    }
}