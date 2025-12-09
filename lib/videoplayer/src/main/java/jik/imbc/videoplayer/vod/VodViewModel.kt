package jik.imbc.videoplayer.vod

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import jik.imbc.videoplayer.player.vod.VodPlayer

class VodViewModel(application: Application) : AndroidViewModel(application = application) {

    val player: VodPlayer = VodPlayer(context = application)


}