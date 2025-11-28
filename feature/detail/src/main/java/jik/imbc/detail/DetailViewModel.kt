package jik.imbc.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import jik.imbc.data.repository.ContentRepository
import jik.imbc.data.repository.ContentRepositoryImpl
import jik.imbc.detail.model.DetailUiState
import jik.imbc.detail.navigation.DetailRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    val contentRepository: ContentRepository = ContentRepositoryImpl()
) : ViewModel() {

    private val contentId = savedStateHandle.toRoute<DetailRoute>().contentId
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)

    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow() // todo: Flow로 변경

    init {
        getContentDetail()
        getRelatedContents()
    }

    private fun getContentDetail() {
        val content = contentRepository.getContentById(contentId).getOrNull()
        if (content != null) {
            _uiState.value = DetailUiState.Success(content = content)
        } else {
            // Handle error case as needed
        }
    }

    private fun getRelatedContents() {
        val relatedContents = contentRepository.getRelatedContents(contentId).getOrNull()

        if (relatedContents != null) {
            val currentState = _uiState.value
            if (currentState is DetailUiState.Success) {
                _uiState.value = currentState.copy(relatedContents = relatedContents)
            }
        } else {
            // Handle error case as needed
        }
    }

    internal fun leaveRating(float: Float) {
        contentRepository.leaveRating(contentId, float)
            .onSuccess {
                getContentDetail()
            }.onFailure {
                // Handle error case as needed
            }
    }

    companion object {
        fun provideFactory(savedStateHandle: SavedStateHandle): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    DetailViewModel(savedStateHandle = savedStateHandle)
                }
            }
    }
}