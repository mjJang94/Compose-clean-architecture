package com.mj.compose_clean_architecture.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mj.compose_clean_architecture.ui.screen.detail.DetailContract
import com.mj.compose_clean_architecture.ui.screen.detail.DetailScreen
import com.mj.compose_clean_architecture.ui.screen.detail.DetailViewModel

@Composable
fun DetailScreenDestination(
    link: String,
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavHostController
) {
    viewModel.configure(link)

    DetailScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            if (navigationEffect is DetailContract.Effect.Navigation.ToMain) {
                navController.popBackStack()
            }
        }
    )
}