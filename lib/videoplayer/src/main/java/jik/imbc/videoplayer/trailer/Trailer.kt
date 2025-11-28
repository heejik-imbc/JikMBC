package jik.imbc.videoplayer.trailer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jik.imbc.videoplayer.Thumbnail

@Composable
fun Trailer(
    modifier: Modifier = Modifier,
    thumbnailUrl: String,
    trailerUrl: String
) {

    Thumbnail(
        modifier = modifier,
        imageUrl = thumbnailUrl,
        play = {}
    )
}