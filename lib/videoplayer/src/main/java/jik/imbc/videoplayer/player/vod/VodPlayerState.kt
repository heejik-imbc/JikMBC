package jik.imbc.videoplayer.player.vod

sealed interface VodPlayerState {
    data object INITIAL : VodPlayerState

    data object PLAYING : VodPlayerState, ActiveState

    data object PAUSED : VodPlayerState, ActiveState

    data object BUFFERING : VodPlayerState, ActiveState

    data object ENDED : VodPlayerState, ActiveState

    data class ERROR(val message: String?, val code: Int?) : VodPlayerState

    companion object {
        val positionValidStates = setOf(PLAYING, PAUSED, BUFFERING)
    }
}

sealed interface ActiveState : VodPlayerState