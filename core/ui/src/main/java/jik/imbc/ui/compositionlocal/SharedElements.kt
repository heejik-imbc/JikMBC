package jik.imbc.ui.compositionlocal

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.staticCompositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope =
    staticCompositionLocalOf<SharedTransitionScope> { error("No SharedTransitionScope") }

val LocalAnimatedContentScope =
    staticCompositionLocalOf<AnimatedContentScope> { error("No AnimatedContentScope") }