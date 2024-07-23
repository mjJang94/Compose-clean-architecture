package com.mj.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mj.feature.detail.navigation.DetailScreenDestination
import com.mj.feature.home.navigation.HomeScreenDestination
import com.mj.presentation.ui.Navigation.Args.NEWS_DATA
import com.mj.presentation.ui.Navigation.Routes.DETAIL
import com.mj.presentation.ui.Navigation.Routes.HOME
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = HOME,
    ) {
        composable(route = HOME) {
            HomeScreenDestination(navController = navController)
        }

        composable(
            route = "$DETAIL/{$NEWS_DATA}",
            arguments = listOf(navArgument(name = NEWS_DATA) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString(NEWS_DATA)
            if (url == null) {
                navController.popBackStack()
            } else {
                DetailScreenDestination(
                    url = URLDecoder.decode(url, StandardCharsets.UTF_8.toString()),
                    navController = navController,
                )
            }
        }
    }
}

object Navigation {
    object Args {
        const val NEWS_DATA = "news_data"
    }

    object Routes {
        const val HOME = "home"
        const val DETAIL = "detail"
    }
}