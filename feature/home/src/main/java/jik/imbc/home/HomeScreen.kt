package jik.imbc.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import jik.imbc.designsystem.state.EmptyLoading
import jik.imbc.home.model.HomeUiState

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier
) {
    val viewModel: HomeViewModel = viewModel()
    val uiState: HomeUiState = viewModel.uiState.collectAsStateWithLifecycle().value

    when (uiState) {
        is HomeUiState.Loading -> {
            EmptyLoading()
        }

        is HomeUiState.Success -> {
            HomeScreen(
                modifier = modifier,
                homeUiState = uiState,
                onClickContent = { contentId -> }
            )
        }
    }
}


@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState.Success,
    onClickContent: (id: String) -> Unit
) {

}