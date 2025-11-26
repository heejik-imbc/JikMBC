package jik.imbc.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import jik.imbc.data.mock.MockDramas
import jik.imbc.designsystem.state.EmptyLoading
import jik.imbc.detail.model.DetailUiState


@Composable
fun DetailRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel()
) {
    val uiState: DetailUiState = viewModel.uiState.collectAsStateWithLifecycle().value

    when (uiState) {
        is DetailUiState.Loading -> {
            EmptyLoading()
        }

        is DetailUiState.Success -> {
            DetailScreen(
                modifier = modifier,
                uiState = uiState
            )
        }
    }
}

@Composable
private fun DetailScreen(
    modifier: Modifier = Modifier,
    uiState: DetailUiState.Success,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = uiState.content.id.toString(),
        )
    }
}


@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        uiState = DetailUiState.Success(MockDramas.first())
    )
}