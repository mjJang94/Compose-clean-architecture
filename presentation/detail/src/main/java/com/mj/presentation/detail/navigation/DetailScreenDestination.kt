package com.mj.presentation.detail.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mj.presentation.detail.DetailContract
import com.mj.presentation.detail.DetailScreen
import com.mj.presentation.detail.DetailViewModel

@Composable
fun DetailScreenDestination(
    url: String,
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavHostController
) {
    viewModel.configure(url)

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