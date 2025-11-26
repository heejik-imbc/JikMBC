package jik.imbc.jikmbc

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import jik.imbc.detail.navigation.detailScreen
import jik.imbc.detail.navigation.navigateDetail
import jik.imbc.home.navigation.HomeRoute
import jik.imbc.home.navigation.homeScreen


@Composable
fun JbcNavHost(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        homeScreen(onClickContent = { contentId, origin ->
            navController.navigateDetail(contentId = contentId, origin = origin)
        })
        detailScreen()
    }
}