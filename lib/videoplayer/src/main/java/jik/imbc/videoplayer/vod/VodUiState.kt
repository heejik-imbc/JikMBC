package jik.imbc.videoplayer.vod

import jik.imbc.model.Content
import jik.imbc.videoplayer.player.vod.VodPlayerState

data class VodUiState(
    val content: Content = Content.EMPTY,
    val playerState: VodPlayerState = VodPlayerState.INITIAL,
    val position: Long = 0,
    val duration: Long = 0
)