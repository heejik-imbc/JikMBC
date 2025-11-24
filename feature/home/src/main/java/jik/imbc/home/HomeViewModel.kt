package jik.imbc.home

import androidx.lifecycle.ViewModel
import jik.imbc.data.repository.ContentRepository
import jik.imbc.data.repository.ContentRepositoryImpl
import jik.imbc.home.model.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val contentRepository: ContentRepository = ContentRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

}