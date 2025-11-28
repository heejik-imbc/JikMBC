package jik.imbc.videoplayer.player.trailer

import androidx.media3.common.Player

internal val trailerPlayerListener: (stateChange: (TrailerPlayerState) -> Unit) -> Player.Listener =
    {
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                when {
                    playbackState != 3 -> it(TrailerPlayerState.INITIAL)
                }
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