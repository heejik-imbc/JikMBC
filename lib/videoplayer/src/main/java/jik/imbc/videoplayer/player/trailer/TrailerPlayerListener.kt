package jik.imbc.videoplayer.player.trailer

import android.annotation.SuppressLint
import androidx.media3.common.MediaItem
import androidx.media3.common.Player

internal val trailerPlayerListener: (stateChange: (TrailerPlayerState) -> Unit) -> Player.Listener =
    {
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                @SuppressLint("SwitchIntDef")
                when (playbackState) {
                    Player.STATE_IDLE, Player.STATE_ENDED -> it(TrailerPlayerState.INITIAL)
                }
            }

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int
            ) {
                super.onMediaItemTransition(mediaItem, reason)
                it(TrailerPlayerState.CAN_PLAY)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) {
                    it(TrailerPlayerState.PLAYING)
                } else {
                    it(TrailerPlayerState.PAUSED)
                }
            }
        }
    }