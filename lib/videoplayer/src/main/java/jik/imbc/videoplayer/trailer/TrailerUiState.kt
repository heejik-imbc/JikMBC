package jik.imbc.videoplayer.trailer

import jik.imbc.videoplayer.player.trailer.TrailerPlayerState

data class TrailerUiState(
    val playerState: TrailerPlayerState = TrailerPlayerState.INITIAL,
    val position: Long = 0,
    val duration: Long = 0
)