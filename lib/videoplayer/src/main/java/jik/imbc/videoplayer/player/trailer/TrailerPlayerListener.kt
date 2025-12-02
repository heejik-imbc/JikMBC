package jik.imbc.videoplayer.player.trailer

import android.annotation.SuppressLint
import androidx.media3.common.MediaItem
import androidx.media3.common.Player

internal val trailerPlayerListener: (changeState: (TrailerPlayerState) -> Unit) -> Player.Listener =
    { changeState ->
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                @SuppressLint("SwitchIntDef")
                when (playbackState) {
                    Player.STATE_IDLE, Player.STATE_ENDED -> changeState(TrailerPlayerState.INITIAL)
                }
            }

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int
            ) {
                super.onMediaItemTransition(mediaItem, reason)
                changeState(TrailerPlayerState.CAN_PLAY)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) {
                    changeState(TrailerPlayerState.PLAYING)
                } else {
                    changeState(TrailerPlayerState.PAUSED)
                }
            }
        }
    }