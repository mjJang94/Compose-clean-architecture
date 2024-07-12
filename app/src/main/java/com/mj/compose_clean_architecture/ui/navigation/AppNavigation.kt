package com.mj.compose_clean_architecture.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mj.compose_clean_architecture.common.ktx.parseJson
import com.mj.compose_clean_architecture.common.ktx.toJson
import com.mj.compose_clean_architecture.data.model.News
import com.mj.compose_clean_architecture.ui.navigation.Navigation.Args.NEWS_DATA
import com.mj.compose_clean_architecture.ui.screen.detail.DetailScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Navigation.Routes.HOME,
    ) {
        composable(
            route = Navigation.Routes.HOME
        ) {
            HomeScreenDestination(navController = navController)
        }

        composable(
            route = Navigation.Routes.DETAIL,
            arguments = listOf(navArgument(NEWS_DATA) {
                type = NavType.StringType
            })
        ) {
            val news = backStackEntry?.arguments?.getString(NEWS_DATA).parseJson<News>()
            if (news == null) {
                navController.popBackStack(Navigation.Routes.HOME, inclusive = false)
            } else {
                DetailScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    news = news
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
        const val DETAIL = "detail/${NEWS_DATA}"
    }
}

fun NavController.navigateToDetail(news: News) {
    val newsJson = news.toJson()
    navigate(route = "${Navigation.Routes.DETAIL}/$newsJson")
}