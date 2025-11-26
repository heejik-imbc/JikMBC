package jik.imbc.jikmbc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import jik.imbc.designsystem.theme.JikMBCTheme
import jik.imbc.ui.transition.LocalSharedTransitionScope


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JikMBCTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets()
                ) { innerPadding ->
                    @OptIn(ExperimentalSharedTransitionApi::class)
                    SharedTransitionLayout(modifier = Modifier.padding(innerPadding)) {
                        CompositionLocalProvider(LocalSharedTransitionScope provides this@SharedTransitionLayout) {
                            JbcNavHost()
                        }
                    }
                }
            }
        }
    }
}