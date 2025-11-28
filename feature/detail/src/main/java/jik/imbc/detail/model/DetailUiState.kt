package jik.imbc.detail.model

import jik.imbc.model.Content

sealed interface DetailUiState {

    data object Loading : DetailUiState

    data class Success(
        val content: Content,
        val relatedContents: List<Content> = emptyList()
    ) : DetailUiState
}