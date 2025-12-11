package jik.imbc.videoplayer.player.vod

import androidx.media3.common.Player

internal val vodPlayerListener: (changeState: (VodPlayerState) -> Unit) -> Player.Listener =
    { changeState ->
        object : Player.Listener {

            override fun onEvents(
                player: Player,
                events: Player.Events
            ) {
                super.onEvents(player, events)

                val error = player.playerError

                val vodPState = when (player.playbackState) {
                    Player.STATE_IDLE -> if (error == null) {
                        VodPlayerState.INITIAL
                    } else {
                        VodPlayerState.ERROR(
                            player.playerError?.message,
                            player.playerError?.errorCode
                        )
                    }

                    Player.STATE_ENDED -> VodPlayerState.ENDED

                    Player.STATE_BUFFERING -> if (player.contentPosition == 0L) {
                        VodPlayerState.INITIAL
                    } else {
                        VodPlayerState.BUFFERING
                    }

                    Player.STATE_READY -> if (player.playWhenReady) {
                        VodPlayerState.PLAYING
                    } else {
                        VodPlayerState.PAUSED
                    }

                    else -> return
                }

                changeState(vodPState)
            }
        }
    }