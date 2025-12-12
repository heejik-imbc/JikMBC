package jik.imbc.videoplayer.vod

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import jik.imbc.data.mock.MockVideo.LONG_VIDEO_URL
import jik.imbc.data.repository.ContentRepository
import jik.imbc.data.repository.ContentRepositoryImpl
import jik.imbc.model.Content
import jik.imbc.videoplayer.data.SettingRepository.SEEK_AMOUNT
import jik.imbc.videoplayer.player.vod.VodPlayer
import jik.imbc.videoplayer.player.vod.VodPlayerState
import jik.imbc.videoplayer.vod.VodActivity.Companion.EXTRA_CONTENT_ID
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class VodViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application = application) {

    val contentRepository: ContentRepository = ContentRepositoryImpl()

    val player: VodPlayer = VodPlayer(context = application)

    val content: Content = requireNotNull(savedStateHandle.get<Int>(EXTRA_CONTENT_ID)).let { id ->
        contentRepository.getContentById(contentId = id).getOrDefault(Content.EMPTY)
    }

    val uiState: StateFlow<VodUiState> = combine(
        player.state,
        player.currentPosition,
        player.duration
    ) { state, position, duration ->
        VodUiState(content = content, playerState = state, position = position, duration = duration)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VodUiState())

    init {
        start()
    }

    fun playPauseReplay() {
        when (uiState.value.playerState) {
            is VodPlayerState.PLAYING -> pause()
            is VodPlayerState.PAUSED -> play()
            is VodPlayerState.ENDED -> replay()
            else -> Unit
        }
    }

    fun skipBack() {
        val newPosition = (uiState.value.position - SEEK_AMOUNT).coerceAtLeast(0)
        player.changePosition(newPosition)
    }

    fun skipForward() {
        val newPosition =
            (uiState.value.position + SEEK_AMOUNT).coerceAtMost(uiState.value.duration)
        player.changePosition(newPosition)
    }

    private fun start() {
        player.start(url = LONG_VIDEO_URL)
    }

    private fun play() {
        player.play()
    }

    private fun pause() {
        player.pause()
    }

    private fun replay() {
        player.player.seekTo(0)
        player.play()
    }

    fun changePosition(position: Long) {
        player.changePosition(position)
    }

    override fun onCleared() {
        super.onCleared()

        player.release()
    }
}