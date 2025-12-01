package jik.imbc.ui.palette

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun extractRepresentativeColor(
    context: Context,
    imageUrl: String
): Int {
    val defaultColor = Color.Transparent.toArgb()

    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .allowHardware(false)
        .build()

    val result = withContext(Dispatchers.IO) {
        Coil.imageLoader(context).execute(request)
    }

    if (result is SuccessResult) {
        val bitmap = result.drawable.toBitmap()
        val palette = Palette.from(bitmap).generate()

        val vibrant = palette.getVibrantColor(defaultColor)
        val dominantColor = palette.getDominantColor(defaultColor)

        return if (vibrant != defaultColor) vibrant else dominantColor
    }

    return defaultColor
}