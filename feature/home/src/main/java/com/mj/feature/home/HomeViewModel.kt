package com.mj.feature.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mj.core.base.BaseViewModel
import com.mj.domain.usecase.GetNewsUseCase
import com.mj.domain.usecase.GetNewsUseCase.GetNewsParam as Param
import com.mj.feature.home.model.NewsInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    private val _newsState: MutableStateFlow<PagingData<NewsInfo.Content>> = MutableStateFlow(PagingData.empty())

    override fun setInitialState() = HomeContract.State(
        newsPagingInfo = MutableStateFlow(PagingData.empty()),
        isLoading = false,
        isError = false,
    )

    override fun handleEvents(event: HomeContract.Event) {
        Log.d("HomeViewModel", "event = $event")
        when (event) {
            is HomeContract.Event.NewsSelection -> setEffect { HomeContract.Effect.Navigation.ToDetail(event.url) }
            is HomeContract.Event.SearchClick -> getNews(event.query)
            is HomeContract.Event.Retry -> getNews(event.query)
        }
    }

    private val maxPageSize: Int = 20
    private val prefetchDistance: Int = 10
    private fun getNews(query: String) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = maxPageSize, prefetchDistance),
                pagingSourceFactory = {
                    NewsPagingSource(
                        load = { query, index ->
                            getNewsUseCase(
                                dispatcher = Dispatchers.IO,
                                param = Param(query, index),
                            )
                        },
                        query = query,
                    )
                }
            ).flow
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _newsState.emit(it)
                    setState { copy(newsPagingInfo = _newsState, isLoading = false) }
                    setEffect { HomeContract.Effect.DataLoaded }
                }
        }
    }
}



