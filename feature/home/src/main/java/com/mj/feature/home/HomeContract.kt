package com.mj.feature.home

import androidx.paging.PagingData
import com.mj.core.base.ViewEvent
import com.mj.core.base.ViewSideEffect
import com.mj.core.base.ViewState
import com.mj.feature.home.model.NewsInfo
import kotlinx.coroutines.flow.StateFlow

class HomeContract {

    sealed class Event: ViewEvent {
        data class Retry(val query: String): Event()
        data class SearchClick(val query: String): Event()
        data class NewsSelection(val url: String): Event()
    }

    data class State(
        val newsPagingInfo: StateFlow<PagingData<NewsInfo.Content>>,
        val isLoading: Boolean,
        val isError: Boolean,
    ): ViewState

    sealed class Effect: ViewSideEffect {
        data object DataLoaded: Effect()

        sealed class Navigation: Effect() {
            data class ToDetail(val url: String): Navigation()
        }
    }
}