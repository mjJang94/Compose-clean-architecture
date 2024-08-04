package com.mj.presentation.home

import androidx.paging.PagingData
import com.mj.core.base.ViewEvent
import com.mj.core.base.ViewSideEffect
import com.mj.core.base.ViewState
import com.mj.presentation.home.model.NewsInfo
import kotlinx.coroutines.flow.StateFlow

class HomeContract {

    sealed class Event: ViewEvent {
        data class Retry(val query: String): Event()
        data class SearchClick(val query: String): Event()
        data class NewsSelection(val url: String): Event()
        data class NewsScrap(val isAdd: Boolean, val content: NewsInfo.Content): Event()
    }

    data class State(
        val searchNewsPagingInfo: StateFlow<PagingData<NewsInfo.Content>>,
        val scrapNewsInfo: StateFlow<List<NewsInfo.Content>>,
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