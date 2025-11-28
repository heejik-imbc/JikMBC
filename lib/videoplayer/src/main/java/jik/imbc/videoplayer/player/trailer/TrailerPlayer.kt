package jik.imbc.videoplayer.player.trailer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow

class TrailerPlayer(context: Context) {

    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    val state = MutableStateFlow(TrailerPlayerState.INITIAL)

    init {
        initialize()
    }

    fun initialize() {
        player.apply {
            addListener(trailerPlayerListener { state.value = it })
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
}