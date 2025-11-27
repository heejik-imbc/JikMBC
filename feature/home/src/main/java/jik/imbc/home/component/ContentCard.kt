package jik.imbc.home.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jik.imbc.model.Content
import jik.imbc.ui.action.onClickWithPressEffect
import jik.imbc.ui.transition.ContentCardElementOrigin
import jik.imbc.ui.transition.ContentCardSharedElementKey
import jik.imbc.ui.transition.LocalAnimatedContentScope
import jik.imbc.ui.transition.LocalSharedTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun ContentCard(
    modifier: Modifier = Modifier,
    content: Content,
    onClick: () -> Unit
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    with(sharedTransitionScope) {
        Column(
            modifier = modifier.onClickWithPressEffect(onClick = onClick)
        ) {
            AsyncImage(
                modifier = Modifier
                    .sizeIn(maxHeight = 160.dp)
                    .aspectRatio(2 / 3f)
                    .sharedElement(
                        sharedContentState = sharedTransitionScope.rememberSharedContentState(
                            key = ContentCardSharedElementKey(
                                contentId = content.id,
                                type = ContentCardElementOrigin.LIST_CARD
                            )
                        ),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .clip(RoundedCornerShape(4.dp)),
                model = content.posterUrl,
                contentDescription = content.description,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit
            )
        }
    }
}