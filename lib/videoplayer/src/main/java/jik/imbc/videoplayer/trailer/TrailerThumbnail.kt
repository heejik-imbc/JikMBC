package jik.imbc.videoplayer.trailer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jik.imbc.videoplayer.icons.VideoPlayerIcons

@Composable
fun TrailerThumbnail(
    modifier: Modifier = Modifier,
    imageUrl: String,
    start: () -> Unit
) {
    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = imageUrl,
            contentDescription = "섬네일",
            contentScale = ContentScale.FillWidth,
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = Color.Black.copy(alpha = 0.6f))
                .align(Alignment.Center)
                .clickable(onClick = start),
        ) {
            Icon(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .align(Alignment.Center),
                imageVector = VideoPlayerIcons.PlayArrow,
                contentDescription = "재생 버튼",
                tint = Color.White
            )
        }
    }
}