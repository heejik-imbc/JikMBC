package jik.imbc.videoplayer.player.vod

sealed interface VodPlayerState {

    interface ActiveState : VodPlayerState

    data object Initial : VodPlayerState

    data object Playing : ActiveState

    data object Paused : ActiveState

    data object Buffering : ActiveState

    data object Ended : ActiveState

    data class Error(val message: String?, val code: Int?) : VodPlayerState

    companion object {
        val positionValidStates = setOf(Playing, Paused, Buffering)
    }
}