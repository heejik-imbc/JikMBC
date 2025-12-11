package jik.imbc.videoplayer.player.vod.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

internal val controllerCenterIconSize = 70.dp

@Composable
internal fun ControllerIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    painterResourceId: Int? = null,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    // 둘 중 하나만 non-null 이어야 함
    require((imageVector != null) xor (painterResourceId != null))

    val iconModifier = modifier
        .size(controllerCenterIconSize)
        .clickable(onClick = onClick)

    when {
        imageVector != null -> {
            Icon(
                modifier = iconModifier,
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = Color.White
            )
        }

        painterResourceId != null -> {
            Icon(
                modifier = iconModifier,
                painter = painterResource(id = painterResourceId),
                contentDescription = contentDescription,
                tint = Color.White
            )
        }
    }
}