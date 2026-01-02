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

    var player: MutableStateFlow<ExoPlayer?> = MutableStateFlow(null)

    val state: MutableStateFlow<TrailerPlayerState> = MutableStateFlow(TrailerPlayerState.INITIAL)

    val duration = MutableStateFlow(C.TIME_UNSET)

    val currentPosition: Flow<Long> = flow {
        emit(0L)

        while (true) {
            if (state.value in positionValidStates) {
                emit(player.value?.currentPosition ?: 0L)
            }
            delay(1.seconds / 30)
        }
    }

    var lastPositionBeforeRelease = C.TIME_UNSET

    fun initialize(url: String) {
        player.value = ExoPlayer.Builder(context).build()
            .apply {
                setMediaItem(MediaItem.fromUri(url), lastPositionBeforeRelease)
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
        lastPositionBeforeRelease = player.value?.currentPosition ?: C.TIME_UNSET
        player.value?.release()
        player.value = null
    }

    fun start() {
        player.value?.apply {
            playWhenReady = true
            prepare()
        }
    }

    fun play() {
        player.value?.play()
    }

    fun pause() {
        player.value?.pause()
    }

    fun changePosition(position: Long) {
        player.value?.seekTo(position)
    }
}