package jik.imbc.home

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import jik.imbc.designsystem.state.EmptyLoading
import jik.imbc.home.component.ContentCard
import jik.imbc.home.model.HomeUiState
import jik.imbc.model.Content
import jik.imbc.ui.effect.EffectColumn
import jik.imbc.ui.palette.ExtractRepresentativeColor

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
    var backgroundColor by remember { mutableStateOf(Color.Transparent) }
    val mainPagerState = rememberPagerState(pageCount = { homeUiState.popularContents.size })

    val currentMainContent = homeUiState.popularContents[mainPagerState.currentPage]
    ExtractRepresentativeColor(imageUrl = currentMainContent.thumbnailUrl) {
        backgroundColor = it
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
    )

    val colorStops = arrayOf(
        0.0f to animatedBackgroundColor,
        0.4f to Color.Transparent
    )

    EffectColumn(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(brush = Brush.verticalGradient(colorStops = colorStops)),
    ) {
        Spacer(modifier = Modifier.height(46.dp))

        MainContents(
            contents = homeUiState.popularContents,
            pagerState = mainPagerState,
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
    pagerState: PagerState,
    onClickContent: (id: Int) -> Unit
) {
    HomePager(
        modifier = modifier,
        contents = contents,
        pagerState = pagerState,
        onClickContent = onClickContent
    )
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