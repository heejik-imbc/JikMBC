package jik.imbc.home.model

import jik.imbc.model.Content


sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Success(
        val dramas: List<Content>,
        val entertainments: List<Content>
    ) : HomeUiState
}