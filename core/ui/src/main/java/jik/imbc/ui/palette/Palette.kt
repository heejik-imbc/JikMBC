package jik.imbc.ui.palette

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size


@Composable
fun ExtractRepresentativeColor(imageUrl: String, onColorExtracted: (Color) -> Unit) {
    val context = LocalContext.current
    val defaultColor = Color.Transparent.toArgb()

    rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .allowHardware(false)
            .build(),
        onState = { painterState ->
            if (painterState is AsyncImagePainter.State.Success) {
                val bitmap = painterState.result.drawable.toBitmap()

                Palette.from(bitmap).generate { palette ->
                    palette?.let {
                        val vibrantColor = it.getVibrantColor(defaultColor)
                        if (vibrantColor != defaultColor) {
                            onColorExtracted(Color(vibrantColor))
                        } else {
                            onColorExtracted(Color(it.getDominantColor(defaultColor)))
                        }
                    }
                }
            }
        }
    )
}