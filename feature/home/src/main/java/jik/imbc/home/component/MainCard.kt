package jik.imbc.home.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import jik.imbc.designsystem.icon.JBCIcons
import jik.imbc.model.Content
import jik.imbc.ui.action.onClickWithPressEffect
import jik.imbc.ui.transition.ContentCardElementOrigin
import jik.imbc.ui.transition.ContentCardSharedElementKey
import jik.imbc.ui.transition.LocalAnimatedContentScope
import jik.imbc.ui.transition.LocalSharedTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun MainCard(
    modifier: Modifier = Modifier,
    content: Content,
    onClick: () -> Unit
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .sizeIn(maxHeight = 300.dp)
                .aspectRatio(2 / 3f)
                .onClickWithPressEffect(onClick = onClick)
                .clip(shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.White, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Poster(
                modifier = Modifier
                    .fillMaxSize()
                    .sharedElement(
                        sharedContentState = sharedTransitionScope.rememberSharedContentState(
                            key = ContentCardSharedElementKey(
                                contentId = content.id,
                                type = ContentCardElementOrigin.MAIN_CARD
                            )
                        ),
                        animatedVisibilityScope = animatedContentScope
                    ),
                url = content.posterUrl,
                description = content.description
            )
            Content(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                title = content.title,
                rating = content.rating.toDouble(),
                releaseYear = content.releaseDate.take(4)
            )
        }
    }
}

@Composable
private fun Poster(
    modifier: Modifier = Modifier,
    url: String,
    description: String
) {
    AsyncImage(
        modifier = modifier,
        model = url,
        contentDescription = description,
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit
    )
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    title: String,
    rating: Double,
    releaseYear: String
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = JBCIcons.Star,
                tint = Color(0xFFFFD250),
                contentDescription = "rating"
            )
            Text(text = "$rating", fontSize = 13.sp)
            Text(text = " • ", fontSize = 13.sp)
            Text(text = releaseYear, fontSize = 13.sp)
        }
    }
}