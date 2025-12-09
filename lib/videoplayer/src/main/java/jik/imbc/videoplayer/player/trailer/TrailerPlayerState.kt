package jik.imbc.videoplayer.player.trailer

sealed interface TrailerPlayerState {
    data object INITIAL : TrailerPlayerState

    data object PLAYING : TrailerPlayerState

    data object PAUSED : TrailerPlayerState

    data object BUFFERING : TrailerPlayerState

    data object ENDED : TrailerPlayerState

    data class ERROR(val message: String?, val code: Int?) : TrailerPlayerState
}