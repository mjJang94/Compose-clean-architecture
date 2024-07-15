package com.mj.compose_clean_architecture.ui.screen.home

import androidx.paging.PagingData
import com.mj.compose_clean_architecture.model.NewsInfo
import com.mj.compose_clean_architecture.ui.base.ViewEvent
import com.mj.compose_clean_architecture.ui.base.ViewSideEffect
import com.mj.compose_clean_architecture.ui.base.ViewState
import com.mj.domain.model.News
import kotlinx.coroutines.flow.Flow

class HomeContract {

    sealed class Event: ViewEvent {
        data class Retry(val query: String): Event()
        data class SearchClick(val query: String): Event()
        data class NewsSelection(val newsInfo: NewsInfo): Event()
    }

    data class State(
        val newsInfo: List<NewsInfo>,
        val isLoading: Boolean,
        val isError: Boolean,
    ): ViewState

    sealed class Effect: ViewSideEffect {
        data object DataLoaded: Effect()

        sealed class Navigation: Effect() {
            data class ToDetail(val newsInfo: NewsInfo): Navigation()
        }
    }
}