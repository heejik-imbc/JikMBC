package jik.imbc.videoplayer.vod

import android.content.Context
import android.content.Intent
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

    companion object {
        internal const val EXTRA_CONTENT_ID = "EXTRA_CONTENT_ID"

        fun newIntent(context: Context, contentId: Int): Intent {
            return Intent(context, VodActivity::class.java).apply {
                putExtra(EXTRA_CONTENT_ID, contentId)
            }
        }
    }
}