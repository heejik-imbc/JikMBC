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

    private val playerManager: TrailerPlayer = TrailerPlayer(context = application)

    val player = playerManager.player

    val uiState: StateFlow<TrailerUiState> = combine(
        playerManager.state,
        playerManager.currentPosition,
        playerManager.duration
    ) { state, position, duration ->
        TrailerUiState(playerState = state, position = position, duration = duration)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TrailerUiState())

    fun initializePlayer(url: String) {
        playerManager.initialize(url)
    }

    fun start() {
        playerManager.start()
    }

    fun playOrPause() {
        when (uiState.value.playerState) {
            TrailerPlayerState.PLAYING -> playerManager.pause()
            TrailerPlayerState.PAUSED -> playerManager.play()
            TrailerPlayerState.ENDED -> replay()
            else -> Unit
        }
    }

    fun replay() {
        playerManager.changePosition(position = 0L)
        playerManager.play()
    }

    fun changePosition(position: Long) {
        playerManager.changePosition(position)
    }

    fun releasePlayer() {
        playerManager.release()
    }
}