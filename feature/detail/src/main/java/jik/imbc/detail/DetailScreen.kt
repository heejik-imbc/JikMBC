package jik.imbc.detail

import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import jik.imbc.data.mock.MockDramas
import jik.imbc.designsystem.state.EmptyLoading
import jik.imbc.detail.component.DetailTopBar
import jik.imbc.detail.model.DetailUiState
import jik.imbc.ui.transition.ContentCardElementOrigin
import jik.imbc.ui.transition.ContentCardSharedElementKey
import jik.imbc.ui.transition.LocalAnimatedContentScope
import jik.imbc.ui.transition.LocalSharedTransitionScope


@Composable
fun DetailRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel,
    origin: ContentCardElementOrigin
) {
    val uiState: DetailUiState = viewModel.uiState.collectAsStateWithLifecycle().value

    when (uiState) {
        is DetailUiState.Loading -> {
            EmptyLoading()
        }

        is DetailUiState.Success -> {
            DetailScreen(
                modifier = modifier,
                uiState = uiState,
                origin = origin
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailScreen(
    modifier: Modifier = Modifier,
    uiState: DetailUiState.Success,
    origin: ContentCardElementOrigin
) {

    val sharedTransitionScope = LocalSharedTransitionScope.current
    val context = LocalContext.current

    with(sharedTransitionScope) {
        Column(modifier = modifier.fillMaxSize()) {
            DetailTopBar(onClickBack = {
                Toast.makeText(context, "Back", Toast.LENGTH_SHORT).show()
            })
            Thumbnail(
                id = uiState.content.id,
                origin = origin,
                imageUrl = uiState.content.thumbnailUrl,
                description = uiState.content.description
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.Thumbnail(
    modifier: Modifier = Modifier,
    id: Int,
    origin: ContentCardElementOrigin,
    imageUrl: String,
    description: String
) {
    val animatedContentScope = LocalAnimatedContentScope.current

    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .sharedElement(
                sharedContentState = rememberSharedContentState(
                    key = ContentCardSharedElementKey(
                        contentId = id,
                        type = origin
                    )
                ),
                animatedVisibilityScope = animatedContentScope
            ),
        model = imageUrl,
        contentDescription = description,
        contentScale = ContentScale.Fit
    )
}


@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        uiState = DetailUiState.Success(MockDramas.first()),
        origin = ContentCardElementOrigin.MAIN_CARD
    )
}