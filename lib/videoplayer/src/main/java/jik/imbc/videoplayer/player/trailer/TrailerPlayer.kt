package jik.imbc.videoplayer.player.trailer

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState.Companion.positionValidStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class TrailerPlayer(val context: Context) {

    var player: ExoPlayer? = null

    val state: MutableStateFlow<TrailerPlayerState> = MutableStateFlow(TrailerPlayerState.INITIAL)

    val duration = MutableStateFlow(C.TIME_UNSET)

    val currentPosition: Flow<Long> = flow {
        emit(0L)

        while (true) {
            if (state.value in positionValidStates) {
                emit(player?.currentPosition ?: 0L)
            }
            delay(1.seconds / 30)
        }
    }

    fun initialize() {
        player = ExoPlayer.Builder(context).build()
            .apply {
                addListener(trailerPlayerListener {
                    state.value = it

                    this@TrailerPlayer.duration.let { duration ->
                        if (duration.value == C.TIME_UNSET) {
                            duration.value = this.duration
                        }
                    }
                })
            }
    }

    fun release() {
        player?.release()
        player = null
    }

    fun start(url: String) {
        player?.apply {
            setMediaItem(MediaItem.fromUri(url))
            playWhenReady = true
            prepare()
        }
    }

    fun play() {
        player?.play()
    }

    fun pause() {
        player?.pause()
    }

    fun changePosition(position: Long) {
        player?.seekTo(position)
    }
}