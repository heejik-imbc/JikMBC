package jik.imbc.videoplayer.pip

import android.app.PictureInPictureParams
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.PictureInPictureModeChangedInfo
import androidx.core.graphics.toRect
import androidx.core.util.Consumer
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun Modifier.setPipForPostAndroid12(player: ExoPlayer, shouldEnterPipMode: Boolean): Modifier {
    val context = LocalContext.current

    return this.onGloballyPositioned { layoutCoordinates ->
        val builder = PictureInPictureParams.Builder()
        if (shouldEnterPipMode && player.videoSize != VideoSize.UNKNOWN) {
            val sourceRect = layoutCoordinates.boundsInWindow().toAndroidRectF().toRect()
            builder.setSourceRectHint(sourceRect)
            builder.setAspectRatio(
                Rational(player.videoSize.width, player.videoSize.height)
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setAutoEnterEnabled(shouldEnterPipMode)
        }
        context.findActivity().setPictureInPictureParams(builder.build())
    }
}

@Composable
internal fun SetPipForPreAndroid12(shouldEnterPipMode: Boolean) {
    val currentShouldEnterPipMode by rememberUpdatedState(shouldEnterPipMode)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        val context = LocalContext.current

        DisposableEffect(key1 = context) {
            val onUserLeaveBehavior = Runnable {
                if (currentShouldEnterPipMode) {
                    context.enterPip()
                }
            }
            context.findActivity().addOnUserLeaveHintListener(onUserLeaveBehavior)

            onDispose {
                context.findActivity().removeOnUserLeaveHintListener(onUserLeaveBehavior)
            }
        }
    }
}

@Composable
internal fun rememberIsInPipMode(): Boolean {
    val activity = LocalContext.current.findActivity()
    var pipMode by remember { mutableStateOf(activity.isInPictureInPictureMode) }

    DisposableEffect(key1 = activity) {
        val observer = Consumer<PictureInPictureModeChangedInfo> { info ->
            pipMode = info.isInPictureInPictureMode
        }

        activity.addOnPictureInPictureModeChangedListener(observer)
        onDispose {
            activity.removeOnPictureInPictureModeChangedListener(observer)
        }
    }

    return pipMode
}

internal fun Context.enterPip() {
    findActivity().enterPictureInPictureMode(PictureInPictureParams.Builder().build())
}


internal fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Picture in picture should be called in the context of an Activity")
}