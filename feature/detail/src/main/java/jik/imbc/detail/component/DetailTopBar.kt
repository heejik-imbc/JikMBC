package jik.imbc.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import jik.imbc.designsystem.icon.JbcIcons.ArrowBack
import jik.imbc.ui.action.onClickWithPressEffect

@Composable
internal fun DetailTopBar(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit
) {
    Row(modifier = modifier) {
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