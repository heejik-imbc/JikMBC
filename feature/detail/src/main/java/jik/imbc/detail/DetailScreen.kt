package jik.imbc.detail

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import jik.imbc.data.mock.MockDramas
import jik.imbc.designsystem.component.JbcChip
import jik.imbc.designsystem.icon.JbcIcons
import jik.imbc.designsystem.state.EmptyLoading
import jik.imbc.detail.component.DetailTopBar
import jik.imbc.detail.model.DetailUiState
import jik.imbc.model.Content
import jik.imbc.ui.action.onClickWithPressEffect
import jik.imbc.ui.count.AnimatedCounter
import jik.imbc.ui.layout.EffectColumn
import jik.imbc.ui.transition.ContentCardElementOrigin
import jik.imbc.ui.transition.ContentCardSharedElementKey
import jik.imbc.ui.transition.LocalAnimatedContentScope
import jik.imbc.ui.transition.LocalSharedTransitionScope
import java.util.Locale


@Composable
fun DetailRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel,
    origin: ContentCardElementOrigin
) {
    val uiState: DetailUiState = viewModel.uiState.collectAsStateWithLifecycle().value

    when (uiState) {
        is DetailUiState.Loading -> {
            EmptyLoading()
        }

        is DetailUiState.Success -> {
            DetailScreen(
                modifier = modifier,
                uiState = uiState,
                origin = origin,
                onRating = viewModel::leaveRating
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailScreen(
    modifier: Modifier = Modifier,
    uiState: DetailUiState.Success,
    origin: ContentCardElementOrigin,
    onRating: (Float) -> Unit
) {

    val sharedTransitionScope = LocalSharedTransitionScope.current
    val context = LocalContext.current
    var ratingExpanded by rememberSaveable { mutableStateOf(false) }

    with(sharedTransitionScope) {
        Column(modifier = modifier) {
            DetailTopBar(onClickBack = {
                Toast.makeText(context, "Back", Toast.LENGTH_SHORT).show()
            })
            Trailer(
                id = uiState.content.id,
                origin = origin,
                imageUrl = uiState.content.thumbnailUrl,
            )
            EffectColumn(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                delayPerItem = 600
            ) {
                EffectColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f))
                        .padding(12.dp),
                    delayPerItem = 300
                ) {
                    MainInfo(
                        modifier = Modifier,
                        title = uiState.content.title,
                        rating = uiState.content.rating.toDouble(),
                        ratingCount = uiState.content.ratingCount,
                        releaseYear = uiState.content.releaseYear,
                        userRating = uiState.content.userRating,
                        ratingExpanded = ratingExpanded,
                        toggleRatingModify = { ratingExpanded = !ratingExpanded },
                        onRating = onRating
                    )
                    Description(
                        modifier = Modifier.padding(vertical = 12.dp),
                        description = uiState.content.description
                    )
                }
                RelatedContents(
                    modifier = Modifier.padding(12.dp),
                    contents = uiState.relatedContents
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.Trailer(
    modifier: Modifier = Modifier,
    id: Int,
    origin: ContentCardElementOrigin,
    imageUrl: String,
) {
    val animatedContentScope = LocalAnimatedContentScope.current

    jik.imbc.videoplayer.trailer.Trailer(
        modifier = modifier
            .fillMaxWidth()
            .sharedBounds(
                sharedContentState = rememberSharedContentState(
                    key = ContentCardSharedElementKey(
                        contentId = id,
                        type = origin
                    )
                ),
                animatedVisibilityScope = animatedContentScope
            ),
        thumbnailUrl = imageUrl,
        trailerUrl = ""
    )
}

@Composable
private fun MainInfo(
    modifier: Modifier = Modifier,
    title: String,
    rating: Double,
    ratingCount: Int,
    releaseYear: String,
    ratingExpanded: Boolean,
    userRating: Float?,
    toggleRatingModify: () -> Unit,
    onRating: (Float) -> Unit
) {
    val defaultExitTransition = fadeOut() + shrinkVertically()
    var ratingSectionExitTransition by remember { mutableStateOf(defaultExitTransition) }

    Column(modifier = modifier) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            ReleaseYearChip(releaseYear = releaseYear)
            Spacer(modifier = Modifier.width(8.dp))
            RatingChip(
                rating = rating,
                ratingCount = ratingCount,
                ratingExpanded = ratingExpanded,
                userRating = userRating,
                toggleRatingModify = {
                    toggleRatingModify()
                    ratingSectionExitTransition = defaultExitTransition
                }
            )
        }
        AnimatedVisibility(visible = ratingExpanded, exit = ratingSectionExitTransition) {
            RatingModifySection(
                userRating = userRating,
                onRating = {
                    onRating(it)
                    ratingSectionExitTransition =
                        fadeOut(animationSpec = tween(durationMillis = 1000)) +
                                shrinkVertically(animationSpec = tween(durationMillis = 1000))
                    toggleRatingModify()
                }
            )
        }
    }
}

@Composable
private fun ReleaseYearChip(
    modifier: Modifier = Modifier,
    releaseYear: String
) {
    JbcChip(modifier = modifier) {
        Text(text = releaseYear, fontSize = 13.sp)
    }
}


enum class RatingModifyMode(val iconRes: ImageVector) {
    CanAdd(iconRes = JbcIcons.Add),
    Modify(iconRes = JbcIcons.Refresh)
}


@Composable
private fun RatingChip(
    modifier: Modifier = Modifier,
    rating: Double,
    ratingCount: Int,
    ratingExpanded: Boolean,
    userRating: Float?,
    toggleRatingModify: () -> Unit
) {
    Log.d("heejik", "userRating: $userRating")

    val mode = if (userRating != null) {
        RatingModifyMode.Modify
    } else {
        RatingModifyMode.CanAdd
    }
    val animatedUserRating by animateFloatAsState(userRating ?: 0f)

    JbcChip(modifier = modifier.height(intrinsicSize = IntrinsicSize.Max)) {
        Row(
            modifier = Modifier.animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = JbcIcons.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFFD250),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "$rating", fontSize = 13.sp)
            Spacer(modifier = Modifier.width(4.dp))
            AnimatedCounter(target = ratingCount) { animatedRatingCount ->
                Text(
                    text = "(${animatedRatingCount})",
                    fontSize = 13.sp,
                    color = Color(0xFF65A1EC)
                )
            }
            VerticalDivider(
                Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 6.dp),
                color = Color.Gray.copy(alpha = 0.4f)
            )
            Box(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .onClickWithPressEffect(
                        scaleFactor = 1f,
                        pressColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        onClick = toggleRatingModify
                    )
            ) {
                AnimatedContent(targetState = mode) {
                    // 유저 별점이 있으면 별점 표시, 없으면 별점 수정 아이콘 표시
                    if (it == RatingModifyMode.CanAdd || ratingExpanded) {
                        val rotation by animateFloatAsState(
                            targetValue = if (ratingExpanded) 45f else 0f,
                            animationSpec = spring(),
                        )
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .rotate(rotation),
                            imageVector = RatingModifyMode.CanAdd.iconRes,
                            contentDescription = if (ratingExpanded) "별점 추가 닫기" else "별점 추가하기",
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = it.iconRes,
                            contentDescription = "별점 수정하기",
                        )
                    }
                }
            }
            if (mode == RatingModifyMode.Modify) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${
                        String.format(
                            Locale.getDefault(),
                            "%.1f",
                            animatedUserRating
                        )
                    })",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFFD250)
                )
            }
        }
    }
}

@Composable
private fun RatingModifySection(
    modifier: Modifier = Modifier,
    userRating: Float?,
    onRating: (Float) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .padding(end = 28.dp)
                .size(16.dp)
                .align(Alignment.End)
        ) {
            val path = Path().apply {
                moveTo(size.width / 2, 0f)
                lineTo(0f, size.height)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(
                path = path,
                color = backgroundColor
            )
        }
        RatingModifySectionContent(
            userRating = userRating,
            onRating = onRating
        )
    }
}

private enum class StarState { Full, Half, Empty }


@Composable
private fun RatingModifySectionContent(
    modifier: Modifier = Modifier,
    userRating: Float?,
    onRating: (Float) -> Unit,
) {
    val maxRating = 5f
    val backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    var widthPx by remember { mutableIntStateOf(0) }
    var isDragging by remember { mutableStateOf(false) }
    var newRating by remember { mutableFloatStateOf(userRating ?: maxRating) }

    val displayRating = if (isDragging) newRating else userRating ?: maxRating

    fun updateRating(x: Float) {
        val clampedX = x.coerceIn(0f, widthPx.toFloat())

        val calculatedRating = (clampedX / widthPx) * 5f
        newRating = (calculatedRating * 2).toInt() / 2f // 가까운 0.5 단위로 반올림
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = backgroundColor)
            .padding(8.dp)
            .onSizeChanged { widthPx = it.width }
            .pointerInput(key1 = Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        updateRating(offset.x)
                    },
                    onDragEnd = {
                        isDragging = false
                        onRating(newRating)
                    },
                    onDragCancel = {
                        isDragging = false
                        newRating = userRating ?: maxRating
                    },
                    onDrag = { change, _ ->
                        updateRating(change.position.x)
                    }
                )
            }
    ) {
        repeat(5) {
            val starState = when {
                displayRating >= it + 1 -> StarState.Full
                displayRating >= it + 0.5f -> StarState.Half
                else -> StarState.Empty
            }

            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = when (starState) {
                    StarState.Full, StarState.Empty -> JbcIcons.Star
                    StarState.Half -> JbcIcons.StarHalf
                },
                contentDescription = "rating",
                tint = if (starState == StarState.Empty) {
                    Color.Gray.copy(alpha = 0.4f)
                } else {
                    Color(0xFFFFD250)
                }
            )
        }
    }
}

@Composable
private fun Description(
    modifier: Modifier = Modifier,
    description: String
) {
    Text(
        modifier = modifier,
        text = description,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
}

@Composable
private fun RelatedContents(
    modifier: Modifier = Modifier,
    contents: List<Content>
) {
    if (contents.isEmpty()) return

    Column(modifier = modifier) {
        Text(
            text = "유사한 콘텐츠",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            contents.forEach { content ->
                AsyncImage(
                    modifier = Modifier
                        .sizeIn(maxHeight = 160.dp)
                        .aspectRatio(2 / 3f)
                        .onClickWithPressEffect(onClick = { })
                        .clip(RoundedCornerShape(4.dp)),
                    model = content.getPosterUrl,
                    contentDescription = content.description,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}


@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        uiState = DetailUiState.Success(MockDramas.first()),
        origin = ContentCardElementOrigin.MAIN_CARD,
        onRating = {}
    )
}