package jik.imbc.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jik.imbc.model.Content
import jik.imbc.ui.effect.onClickWithPressEffect

@Composable
internal fun ContentCard(
    modifier: Modifier = Modifier,
    content: Content,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier.onClickWithPressEffect(onClick = onClick)
    ) {
        AsyncImage(
            modifier = Modifier
                .sizeIn(maxHeight = 160.dp)
                .aspectRatio(2 / 3f),
            model = content.thumbnailUrl,
            contentDescription = content.description,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit
        )
    }
}