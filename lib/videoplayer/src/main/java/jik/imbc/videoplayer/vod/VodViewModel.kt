package jik.imbc.videoplayer.vod

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import jik.imbc.data.repository.ContentRepository
import jik.imbc.data.repository.ContentRepositoryImpl
import jik.imbc.model.Content
import jik.imbc.videoplayer.data.SettingRepository
import jik.imbc.videoplayer.player.vod.VodPlayer
import jik.imbc.videoplayer.player.vod.VodPlayerState
import jik.imbc.videoplayer.vod.VodActivity.Companion.EXTRA_CONTENT_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class VodViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application = application) {

    val contentRepository: ContentRepository = ContentRepositoryImpl()
    val settingRepository: SettingRepository = SettingRepository()

    val player: VodPlayer = VodPlayer(context = application)

    private val content: MutableStateFlow<Content> = MutableStateFlow(getContent())

    val uiState: StateFlow<VodUiState> = combine(
        content,
        player.state,
        player.currentPosition,
        player.duration,
        settingRepository.seekAmount
    ) { content, state, position, duration, seekAmount ->
        VodUiState(content = content, playerState = state, position = position, duration = duration, seekAmount = seekAmount)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VodUiState())

    init {
        start()
    }

    fun playPauseReplay() {
        when (uiState.value.playerState) {
            is VodPlayerState.Playing -> pause()
            is VodPlayerState.Paused -> play()
            is VodPlayerState.Ended -> replay()
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

    fun changePosition(position: Long) {
        player.changePosition(position)
    }

    fun changeSeekAmount() {
        settingRepository.changeSeekAmount()
    }

    fun setNewContent(contentId: Int?) {
        if (savedStateHandle.get<Int>(EXTRA_CONTENT_ID) == contentId) return

        savedStateHandle[EXTRA_CONTENT_ID] = requireNotNull(contentId)
        content.value = getContent()
        start()
    }

    fun start() {
        player.start(url = content.value.videoUrl)
    }

    private fun getContent(): Content {
        val contentId = requireNotNull(savedStateHandle.get<Int>(EXTRA_CONTENT_ID))
        return contentRepository.getContentById(contentId = contentId).getOrDefault(Content.EMPTY)
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


    override fun onCleared() {
        super.onCleared()

        player.release()
    }
}