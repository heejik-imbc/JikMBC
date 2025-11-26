package jik.imbc.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jik.imbc.detail.DetailRoute
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(val contentId: Int)

fun NavController.navigateDetail(
    contentId: Int,
    navOptions: NavOptions? = null
) = navigate(route = DetailRoute(contentId), navOptions)

fun NavGraphBuilder.detailScreen() {
    composable<DetailRoute> {
        DetailRoute()
    }
}