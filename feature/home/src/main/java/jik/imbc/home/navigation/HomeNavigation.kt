package jik.imbc.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jik.imbc.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeScreen(onClickContent: (Int) -> Unit) {
    composable<HomeRoute> {
        HomeRoute(onClickContent = onClickContent)
    }
}