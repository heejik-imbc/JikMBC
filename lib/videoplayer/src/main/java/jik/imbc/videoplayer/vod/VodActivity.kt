package jik.imbc.videoplayer.vod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import jik.imbc.designsystem.theme.JikMBCTheme

class VodActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JikMBCTheme {
                VodRoute()
            }
        }
    }
}