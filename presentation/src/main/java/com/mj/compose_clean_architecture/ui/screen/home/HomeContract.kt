package com.mj.compose_clean_architecture.ui.screen.home

import com.mj.compose_clean_architecture.data.model.News
import com.mj.compose_clean_architecture.ui.base.ViewEvent
import com.mj.compose_clean_architecture.ui.base.ViewSideEffect
import com.mj.compose_clean_architecture.ui.base.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeContract {

    sealed class Event: ViewEvent {
        data class Retry(val query: String): Event()
        data class SearchClick(val query: String): Event()
        data class NewsSelection(val news: News): Event()
    }

    data class State(
        val news: List<News>,
        val isLoading: Boolean,
        val isError: Boolean,
    ): ViewState

    sealed class Effect: ViewSideEffect {
        data object DataLoaded: Effect()

        sealed class Navigation: Effect() {
            data class ToDetail(val news: News): Navigation()
        }
    }
}