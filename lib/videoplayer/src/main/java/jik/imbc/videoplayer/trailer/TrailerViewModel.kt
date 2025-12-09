package jik.imbc.videoplayer.trailer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jik.imbc.videoplayer.player.trailer.TrailerPlayer
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class TrailerViewModel(application: Application) : AndroidViewModel(application = application) {

    val player: TrailerPlayer = TrailerPlayer(context = application)
    val uiState: StateFlow<TrailerUiState> = combine(
        player.state,
        player.currentPosition,
        player.duration
    ) { state, position, duration ->
        TrailerUiState(playerState = state, position = position, duration = duration)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TrailerUiState())

    fun start(url: String) {
        player.start(url = url)
    }

    fun playOrPause() {
        when (uiState.value.playerState) {
            TrailerPlayerState.PLAYING -> player.pause()
            TrailerPlayerState.PAUSED -> player.play()
            TrailerPlayerState.ENDED -> replay()
            else -> Unit
        }
    }

    fun replay() {
        player.player.seekTo(0)
        player.play()
    }

    fun changePosition(position: Long) {
        player.changePosition(position)
    }

    fun releasePlayer() {
        player.release()
    }
}