package com.mj.compose_clean_architecture.ui.screen.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.mj.compose_clean_architecture.model.NewsInfo
import com.mj.compose_clean_architecture.ui.base.BaseViewModel
import com.mj.domain.model.News
import com.mj.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    private val _newsState: MutableStateFlow<PagingData<NewsInfo>> = MutableStateFlow(PagingData.empty())

    override fun setInitialState() = HomeContract.State(
        newsPagingInfo = MutableStateFlow(PagingData.empty()),
        isLoading = false,
        isError = false,
    )

    override fun handleEvents(event: HomeContract.Event) {
        Log.d("HomeViewModel", "event = $event")
        when (event) {
            is HomeContract.Event.NewsSelection -> setEffect { HomeContract.Effect.Navigation.ToDetail(event.newsInfo) }
            is HomeContract.Event.SearchClick -> getNews(event.query)
            is HomeContract.Event.Retry -> getNews(event.query)
        }
    }

    private fun getNews(query: String) {
        viewModelScope.launch {
            getNewsUseCase(query)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _newsState.emit(it.translate())
                    setState { copy(newsPagingInfo = _newsState, isLoading = false) }
                    setEffect { HomeContract.Effect.DataLoaded }
                }
        }
    }

    private fun PagingData<News>.translate(): PagingData<NewsInfo> = this.map {
        NewsInfo(
            it.title,
            it.description,
            it.date,
            it.link
        )
    }
}