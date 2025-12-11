package jik.imbc.videoplayer.screen

import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

internal fun ComponentActivity.applyImmersiveMode() {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        WindowInsetsCompat.CONSUMED
    }
}