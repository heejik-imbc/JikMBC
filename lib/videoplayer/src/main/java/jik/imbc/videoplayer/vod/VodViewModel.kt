package jik.imbc.videoplayer.vod

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import jik.imbc.data.mock.MockVideo.LONG_VIDEO_URL
import jik.imbc.data.repository.ContentRepository
import jik.imbc.data.repository.ContentRepositoryImpl
import jik.imbc.model.Content
import jik.imbc.videoplayer.data.SettingRepository
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
    val settingRepository: SettingRepository = SettingRepository()

    val player: VodPlayer = VodPlayer(context = application)

    val content: Content = requireNotNull(savedStateHandle.get<Int>(EXTRA_CONTENT_ID)).let { id ->
        contentRepository.getContentById(contentId = id).getOrDefault(Content.EMPTY)
    }

    val uiState: StateFlow<VodUiState> = combine(
        player.state,
        player.currentPosition,
        player.duration,
        settingRepository.seekAmount
    ) { state, position, duration, seekAmount ->
        VodUiState(content = content, playerState = state, position = position, duration = duration, seekAmount = seekAmount)
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
        val newPosition = (uiState.value.position - uiState.value.seekAmount).coerceAtLeast(0)
        player.changePosition(newPosition)
    }

    fun skipForward() {
        val newPosition =
            (uiState.value.position + uiState.value.seekAmount).coerceAtMost(uiState.value.duration)
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
        player.changePosition(position = 0L)
        player.play()
    }

    fun changePosition(position: Long) {
        player.changePosition(position)
    }

    fun changeSeekAmount() {
        settingRepository.changeSeekAmount()
    }

    override fun onCleared() {
        super.onCleared()

        player.release()
    }
}