package jik.imbc.detail

import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import jik.imbc.data.mock.MockDramas
import jik.imbc.designsystem.component.JbcChip
import jik.imbc.designsystem.icon.JbcIcons
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
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                MainInfo(
                    title = uiState.content.title,
                    rating = uiState.content.rating.toDouble(),
                    ratingCount = uiState.content.ratingCount,
                    releaseYear = uiState.content.releaseYear
                )
                Description(
                    description = uiState.content.description
                )
            }
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

@Composable
private fun MainInfo(
    modifier: Modifier = Modifier,
    title: String,
    rating: Double,
    ratingCount: Int,
    releaseYear: String
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            ReleaseYearChip(releaseYear = releaseYear)
            Spacer(modifier = Modifier.width(8.dp))
            RatingChip(
                rating = rating,
                ratingCount = ratingCount
            )
        }
    }
}

@Composable
private fun ReleaseYearChip(
    modifier: Modifier = Modifier,
    releaseYear: String
) {
    JbcChip(modifier = modifier) {
        Text(text = releaseYear, fontSize = 13.sp)
    }
}


@Composable
private fun RatingChip(
    modifier: Modifier = Modifier,
    rating: Double,
    ratingCount: Int
) {
    JbcChip(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = JbcIcons.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFFD250),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "$rating", fontSize = 13.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "($ratingCount)", fontSize = 13.sp, color = Color(0xFF65A1EC))
        }
    }
}


@Composable
private fun Description(
    modifier: Modifier = Modifier,
    description: String
) {

}


@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        uiState = DetailUiState.Success(MockDramas.first()),
        origin = ContentCardElementOrigin.MAIN_CARD
    )
}