package jik.imbc.videoplayer.data

import kotlinx.coroutines.flow.MutableStateFlow

class SettingRepository {

    val seekAmounts = listOf(5_000L, 10_000L, 15_000L, 30_000L)

    val seekAmount: MutableStateFlow<Long> = MutableStateFlow(seekAmounts[1])

    fun changeSeekAmount() {
        val currentIndex = seekAmounts.indexOf(seekAmount.value)
        val nextIndex = (currentIndex + 1) % seekAmounts.size
        seekAmount.value = seekAmounts[nextIndex]
    }
}

