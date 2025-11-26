package jik.imbc.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import jik.imbc.data.repository.ContentRepository
import jik.imbc.data.repository.ContentRepositoryImpl
import jik.imbc.detail.model.DetailUiState
import jik.imbc.detail.navigation.DetailRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class DetailViewModel(
    savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf("contentId" to 1)),
    val contentRepository: ContentRepository = ContentRepositoryImpl()
) : ViewModel() {

    private val contentId = savedStateHandle.toRoute<DetailRoute>().contentId
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)

    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        getContentDetail()
    }

    private fun getContentDetail() {
        val content = contentRepository.getContentById(contentId).getOrNull()

        if (content != null) {
            _uiState.value = DetailUiState.Success(content = content)
        } else {
            // Handle error case as needed
        }
    }
}