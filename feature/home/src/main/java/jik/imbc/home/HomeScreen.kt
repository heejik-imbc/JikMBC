package jik.imbc.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import jik.imbc.designsystem.state.EmptyLoading
import jik.imbc.home.component.ContentCard
import jik.imbc.home.model.HomeUiState
import jik.imbc.model.Content
import jik.imbc.ui.layout.EffectColumn
import jik.imbc.ui.palette.ExtractRepresentativeColor

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onClickContent: (Int) -> Unit
) {
    val uiState: HomeUiState = viewModel.uiState.collectAsStateWithLifecycle().value

    when (uiState) {
        is HomeUiState.Loading -> {
            EmptyLoading()
        }

        is HomeUiState.Success -> {
            HomeScreen(
                modifier = modifier,
                uiState = uiState,
                onClickContent = onClickContent
            )
        }
    }
}


@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState.Success,
    onClickContent: (id: Int) -> Unit
) {
    var backgroundColor by rememberSaveable { mutableIntStateOf(Color.Transparent.toArgb()) }
    val mainPagerState = rememberPagerState(pageCount = { uiState.popularContents.size })

    val currentMainContent = uiState.popularContents[mainPagerState.currentPage]
    ExtractRepresentativeColor(imageUrl = currentMainContent.thumbnailUrl) {
        backgroundColor = it.toArgb()
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = Color(backgroundColor),
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
        MainContents(
            modifier = Modifier.padding(top = 46.dp),
            contents = uiState.popularContents,
            pagerState = mainPagerState,
            onClickContent = onClickContent
        )

        ContentList(
            contents = uiState.dramas,
            onClickContent = onClickContent
        )
        ContentList(
            contents = uiState.entertainments,
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
        uiState = HomeUiState.Success(
            popularContents = emptyList(),
            dramas = emptyList(),
            entertainments = emptyList()
        ),
        onClickContent = {}
    )
}