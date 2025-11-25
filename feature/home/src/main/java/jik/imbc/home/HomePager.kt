package jik.imbc.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import jik.imbc.home.component.MainCard
import jik.imbc.model.Content

@Composable
fun HomePager(
    modifier: Modifier = Modifier,
    contents: List<Content>,
    pagerState: PagerState,
    onClickContent: (id: Int) -> Unit
) {
    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
        ) { page ->
            Box(modifier = Modifier.fillMaxWidth()) {
                MainCard(
                    modifier = Modifier.align(Alignment.Center),
                    content = contents[page],
                    onClick = { onClickContent(contents[page].id) }
                )
            }
        }
        HomeIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 12.dp),
            currentPage = pagerState.currentPage,
            count = pagerState.pageCount
        )
    }
}

@Composable
fun HomeIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    count: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        repeat(count) {
            HomeIndicatorItem(isSelected = it == currentPage)
        }
    }
}

@Composable
fun HomeIndicatorItem(isSelected: Boolean) {
    val color = if (isSelected) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    }

    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color = color)
    )
}
