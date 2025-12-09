package jik.imbc.videoplayer.player.trailer

import androidx.media3.common.Player

internal val trailerPlayerListener: (changeState: (TrailerPlayerState) -> Unit) -> Player.Listener =
    { changeState ->
        object : Player.Listener {

            override fun onEvents(
                player: Player,
                events: Player.Events
            ) {
                super.onEvents(player, events)

                val error = player.playerError

                val trailerState = when (player.playbackState) {
                    Player.STATE_IDLE -> if (error == null) {
                        TrailerPlayerState.INITIAL
                    } else {
                        TrailerPlayerState.ERROR(
                            player.playerError?.message,
                            player.playerError?.errorCode
                        )
                    }

                    Player.STATE_ENDED -> TrailerPlayerState.ENDED

                    Player.STATE_BUFFERING -> if (player.contentPosition == 0L) {
                        TrailerPlayerState.INITIAL
                    } else {
                        TrailerPlayerState.BUFFERING
                    }

                    Player.STATE_READY -> if (player.playWhenReady) {
                        TrailerPlayerState.PLAYING
                    } else {
                        TrailerPlayerState.PAUSED
                    }

                    else -> return
                }

                changeState(trailerState)
            }
        }
    }