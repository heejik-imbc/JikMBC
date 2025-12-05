package jik.imbc.videoplayer.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VPSlider(
    modifier: Modifier = Modifier,
    state: SliderState = rememberSliderState(),
) {

    Slider(
        modifier = modifier,
        state = state,
//        thumb = {},
//        track = {}
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun VPSliderPreview() {
    VPSlider()
}