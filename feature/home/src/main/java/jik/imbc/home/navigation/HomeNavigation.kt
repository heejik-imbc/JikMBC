package jik.imbc.home.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jik.imbc.home.HomeRoute
import jik.imbc.ui.compositionlocal.LocalAnimatedContentScope
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeScreen(onClickContent: (Int) -> Unit) {
    composable<HomeRoute> {
        CompositionLocalProvider(LocalAnimatedContentScope provides this) {
            HomeRoute(
                onClickContent = onClickContent,
            )
        }
    }
}