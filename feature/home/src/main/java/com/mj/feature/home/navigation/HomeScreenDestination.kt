package com.mj.feature.home.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mj.feature.home.HomeContract
import com.mj.feature.home.HomeScreen
import com.mj.feature.home.HomeViewModel
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
                navController.navigateToDetail(navigationEffect.url)
            }
        },
    )
}

fun NavHostController.navigateToDetail(url: String) {
    URLEncoder.encode(url, StandardCharsets.UTF_8.toString()).also { encodeUrl ->
        navigate(route = "detail/$encodeUrl")
    }
}