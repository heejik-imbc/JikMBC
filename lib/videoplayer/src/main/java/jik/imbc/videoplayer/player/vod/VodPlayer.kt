package jik.imbc.videoplayer.player.vod

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import jik.imbc.videoplayer.player.vod.VodPlayerState.Companion.positionValidStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class VodPlayer(context: Context) {

    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    val state: MutableStateFlow<VodPlayerState> = MutableStateFlow(VodPlayerState.Initial)

    val duration = MutableStateFlow(C.TIME_UNSET)

    val currentPosition = flow {
        emit(0L)

        while (true) {
            if (state.value in positionValidStates) {
                emit(player.currentPosition)
            }
            delay(1.seconds / 20)
        }
    }

    init {
        initialize()
    }

    fun initialize() {
        player.apply {
            addListener(vodPlayerListener {
                state.value = it

                this@VodPlayer.duration.let { duration ->
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