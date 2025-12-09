package jik.imbc.videoplayer.vod

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import jik.imbc.videoplayer.player.vod.VodPlayer
import jik.imbc.videoplayer.vod.VodActivity.Companion.EXTRA_CONTENT_ID

class VodViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application = application) {

    val player: VodPlayer = VodPlayer(context = application)

    val contentId: Int = requireNotNull(savedStateHandle.get<Int>(EXTRA_CONTENT_ID))

    init {
        Log.d("heejik", "contentId: $contentId")
    }
}