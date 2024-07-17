package com.mj.compose_clean_architecture.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mj.compose_clean_architecture.ui.navigation.Navigation.Args.NEWS_DATA
import com.mj.compose_clean_architecture.ui.navigation.Navigation.Routes.DETAIL
import com.mj.compose_clean_architecture.ui.screen.home.HomeContract
import com.mj.compose_clean_architecture.ui.screen.home.HomeScreen
import com.mj.compose_clean_architecture.ui.screen.home.HomeViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreenDestination(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    HomeScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            if (navigationEffect is HomeContract.Effect.Navigation.ToDetail) {
                navController.navigateToDetail(navigationEffect.newsInfo.link)
            }
        },
    )
}

fun NavHostController.navigateToDetail(url: String) {
    val encodeUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    navigate(route = "$DETAIL/$encodeUrl")
}