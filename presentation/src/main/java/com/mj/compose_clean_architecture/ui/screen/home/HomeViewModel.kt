package com.mj.compose_clean_architecture.ui.screen.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mj.compose_clean_architecture.model.NewsInfo
import com.mj.compose_clean_architecture.ui.base.BaseViewModel
import com.mj.domain.model.News
import com.mj.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    override fun setInitialState() = HomeContract.State(
        newsInfo = emptyList(),
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
            setState { copy(isLoading = true, isError = false) }
            runCatching {
                getNewsUseCase(query)
            }.onSuccess { news ->
                setState { copy(newsInfo = news.translate(), isLoading = false) }
                setEffect { HomeContract.Effect.DataLoaded }
            }.onFailure { tr ->
                Log.w("HomeViewModel", "$tr")
                setState { copy(isLoading = false, isError = true) }
            }
        }
    }

    private fun List<News>.translate(): List<NewsInfo> = this.map {
        NewsInfo(
            it.title,
            it.description,
            it.date,
            it.link
        )
    }
}