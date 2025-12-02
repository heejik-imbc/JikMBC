package jik.imbc.videoplayer.trailer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jik.imbc.videoplayer.player.trailer.TrailerPlayer
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class TrailerViewModel(application: Application) : AndroidViewModel(application = application) {

    val player: TrailerPlayer = TrailerPlayer(context = application)
    val uiState: StateFlow<TrailerUiState> = player.state
        .map { playerState -> TrailerUiState(playerState = playerState) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TrailerUiState())

    fun start(url: String) {
        player.start(url = url)
    }

    fun playOrPause() {
        when (uiState.value.playerState) {
            TrailerPlayerState.PLAYING -> player.pause()
            TrailerPlayerState.PAUSED -> player.play()
            else -> Unit
        }
    }

    fun releasePlayer() {
        player.release()
    }
}