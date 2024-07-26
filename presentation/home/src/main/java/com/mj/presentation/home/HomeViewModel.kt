package com.mj.presentation.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.mj.core.base.BaseViewModel
import com.mj.domain.model.News
import com.mj.domain.usecase.GetNewsUseCase
import com.mj.domain.usecase.GetNewsUseCase.GetNewsParam as Param
import com.mj.presentation.home.model.NewsInfo
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

    private inner class NewsPagingSource(
        private val load: suspend (query: String, index: Int) -> News,
        private val query: String,
    ) : PagingSource<Int, NewsInfo.Content>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsInfo.Content> {
            return try {
                val currentPage = params.key ?: 1
                val news = load.invoke(query, currentPage).translate()

                LoadResult.Page(
                    data = news.contents,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = news.currentPage + 1
                )
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, NewsInfo.Content>): Int? {
            return state.anchorPosition
        }

        private fun News.translate(): NewsInfo =
            NewsInfo(
                currentPage = this.currentPage,
                contents = this.contents.map { content ->
                    NewsInfo.Content(
                        title = content.title,
                        description = content.description,
                        date = content.date,
                        link = content.link,
                    )
                }
            )
    }
}



