package jik.imbc.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import jik.imbc.designsystem.icon.JBCIcons.ArrowBack
import jik.imbc.ui.action.onClickWithPressEffect

@Composable
internal fun DetailTopBar(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit
) {
    Row(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(shape = CircleShape)
                .onClickWithPressEffect(
                    scaleFactor = 1f,
                    pressColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    onClick = onClickBack
                )
        ) {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
                imageVector = ArrowBack,
                contentDescription = "Back"
            )
        }
    }

}