package jik.imbc.videoplayer.vod

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
internal fun VodRoute(
    modifier: Modifier = Modifier,
    viewModel: VodViewModel = viewModel()
) {

    VodScreen(
        modifier = modifier
    )
}

@Composable
private fun VodScreen(
    modifier: Modifier = Modifier,
) {

}

@Preview
@Composable
private fun VodScreenPreview() {
}