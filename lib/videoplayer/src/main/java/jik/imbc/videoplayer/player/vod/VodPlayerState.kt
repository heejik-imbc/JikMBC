package jik.imbc.videoplayer.player.vod

sealed interface VodPlayerState {
    data object INITIAL : VodPlayerState

    data object PLAYING : VodPlayerState

    data object PAUSED : VodPlayerState

    data object BUFFERING : VodPlayerState

    data object ENDED : VodPlayerState

    data class ERROR(val message: String?, val code: Int?) : VodPlayerState

    companion object {
        val positionValidStates = setOf(PLAYING, PAUSED, BUFFERING,)
    }
}