package jik.imbc.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import jik.imbc.designsystem.state.EmptyLoading
import jik.imbc.home.component.ContentCard
import jik.imbc.home.model.HomeUiState
import jik.imbc.model.Content

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier
) {
    val viewModel: HomeViewModel = viewModel()
    val uiState: HomeUiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    when (uiState) {
        is HomeUiState.Loading -> {
            EmptyLoading()
        }

        is HomeUiState.Success -> {
            HomeScreen(
                modifier = modifier,
                homeUiState = uiState,
                onClickContent = { contentId ->
                    Toast.makeText(context, "contentId: $contentId", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}


@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState.Success,
    onClickContent: (id: Int) -> Unit
) {

    Column(
        modifier = modifier
    ) {
        MainContents(
            contents = homeUiState.popularContents,
            onClickContent = onClickContent
        )

        ContentList(
            contents = homeUiState.dramas,
            onClickContent = onClickContent
        )
        ContentList(
            contents = homeUiState.entertainments,
            onClickContent = onClickContent
        )
    }
}

@Composable
private fun MainContents(
    modifier: Modifier = Modifier,
    contents: List<Content>,
    onClickContent: (id: Int) -> Unit
) {
    Box(modifier = modifier) {

    }
}

@Composable
private fun ContentList(
    modifier: Modifier = Modifier,
    contents: List<Content>,
    onClickContent: (id: Int) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = contents, key = { it.id }) { content ->
            ContentCard(
                content = content,
                onClick = { onClickContent(content.id) }
            )
        }
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        homeUiState = HomeUiState.Success(
            popularContents = emptyList(),
            dramas = emptyList(),
            entertainments = emptyList()
        ),
        onClickContent = {}
    )
}