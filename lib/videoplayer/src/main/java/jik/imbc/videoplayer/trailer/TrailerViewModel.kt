package jik.imbc.videoplayer.trailer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jik.imbc.videoplayer.player.trailer.TrailerPlayer
import jik.imbc.videoplayer.player.trailer.TrailerPlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class TrailerViewModel(application: Application) : AndroidViewModel(application = application) {

    private val playerManager: TrailerPlayer = TrailerPlayer(context = application)
    val player = MutableStateFlow(playerManager.player)

    val uiState: StateFlow<TrailerUiState> = combine(
        playerManager.state,
        playerManager.currentPosition,
        playerManager.duration
    ) { state, position, duration ->
        TrailerUiState(playerState = state, position = position, duration = duration)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TrailerUiState())

    private var shouldResumePlayback = false
    var autoPlayed = false

    fun initialize() {
        playerManager.initialize()
        Log.d("heejik", "initialize!!")
    }

    fun start(url: String) {
        playerManager.start(url = url)
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

    fun pauseIfPlaying() {
        shouldResumePlayback = uiState.value.playerState == TrailerPlayerState.PLAYING
        if (shouldResumePlayback) {
            playerManager.pause()
        }
    }

    fun resumeIfWasPlaying() {
        if (shouldResumePlayback) {
            playerManager.play()
        }
    }

    fun releasePlayer() {
        playerManager.release()
    }
}