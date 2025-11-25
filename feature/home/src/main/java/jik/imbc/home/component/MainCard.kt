package jik.imbc.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jik.imbc.model.Content
import jik.imbc.ui.effect.onClickWithPressEffect

@Composable
internal fun MainCard(
    modifier: Modifier = Modifier,
    content: Content,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.onClickWithPressEffect(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Poster(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.White, RoundedCornerShape(8.dp)),
            url = content.thumbnailUrl,
            description = content.description
        )
    }
}

@Composable
private fun Poster(
    modifier: Modifier = Modifier,
    url: String,
    description: String
) {
    AsyncImage(
        modifier = modifier
            .sizeIn(maxHeight = 300.dp)
            .aspectRatio(2 / 3f),
        model = url,
        contentDescription = description,
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit
    )
}