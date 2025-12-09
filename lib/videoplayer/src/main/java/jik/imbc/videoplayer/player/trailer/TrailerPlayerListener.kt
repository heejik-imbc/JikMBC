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

                when (player.playbackState) {
                    Player.STATE_IDLE -> if (player.playerError == null) {
                        changeState(TrailerPlayerState.INITIAL)
                    } else {
                        changeState(
                            TrailerPlayerState.ERROR(
                                player.playerError?.message,
                                player.playerError?.errorCode
                            )
                        )
                    }

                    Player.STATE_ENDED -> changeState(TrailerPlayerState.ENDED)

                    Player.STATE_BUFFERING -> changeState(TrailerPlayerState.BUFFERING)

                    Player.STATE_READY -> if (player.playWhenReady) changeState(TrailerPlayerState.PLAYING) else changeState(
                        TrailerPlayerState.PAUSED
                    )
                }
            }
        }
    }