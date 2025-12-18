package jik.imbc.videoplayer.vod

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import jik.imbc.designsystem.theme.JikMBCTheme
import jik.imbc.videoplayer.screen.applyImmersiveMode

class VodActivity : ComponentActivity() {
    lateinit var vodViewModel: VodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        applyImmersiveMode()

        setContent {
            vodViewModel = viewModel()
            JikMBCTheme {
                VodRoute(viewModel = vodViewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        vodViewModel.setNewContent(contentId = intent.getIntExtra(EXTRA_CONTENT_ID, -1).takeIf { it != -1 })
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