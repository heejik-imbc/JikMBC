package jik.imbc.detail.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import jik.imbc.detail.DetailRoute
import jik.imbc.detail.DetailViewModel
import jik.imbc.ui.compositionlocal.LocalAnimatedContentScope
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(val contentId: Int)

fun NavController.navigateDetail(
    contentId: Int,
    navOptions: NavOptions? = null
) = navigate(route = DetailRoute(contentId), navOptions)

fun NavGraphBuilder.detailScreen() {
    composable<DetailRoute> {
        CompositionLocalProvider(LocalAnimatedContentScope provides this) {
            DetailRoute(
                viewModel = viewModel(
                    factory = DetailViewModel.provideFactory(savedStateHandle = it.savedStateHandle)
                )
            )
        }
    }
}