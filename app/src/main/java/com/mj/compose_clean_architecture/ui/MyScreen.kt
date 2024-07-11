package com.mj.compose_clean_architecture.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mj.compose_clean_architecture.common.ktx.parseJson
import com.mj.compose_clean_architecture.common.ktx.toJson
import com.mj.compose_clean_architecture.ui.model.News
import com.mj.compose_clean_architecture.ui.screen.detail.DetailScreen
import com.mj.compose_clean_architecture.ui.screen.home.HomeScreen
import com.mj.compose_clean_architecture.ui.screen.home.HomeViewModel

enum class MyScreen {
    HOME, DETAIL
}


@Composable
fun MyApp(
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val query by viewModel.query.collectAsState()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = MyScreen.HOME.name,
    ) {
        composable(route = MyScreen.HOME.name) {
            HomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                query = query,
                onQueryChange = { input -> viewModel.updateQuery(input) },
                onItemClick = { news ->
                    val newsJson = news.toJson()
                    navController.navigate("${MyScreen.DETAIL.name}/$newsJson")
                }
            )
        }

        composable(
            route = "${MyScreen.DETAIL.name}/{news}",
            arguments = listOf(navArgument("news") { type = NavType.StringType })
        ) {
            val news = backStackEntry?.arguments?.getString("news").parseJson<News>()

            if (news == null) {
                navController.popBackStack(MyScreen.HOME.name, inclusive = false)
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