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
import com.mj.domain.usecase.AddScrapNewsUseCase
import com.mj.domain.usecase.DeleteScrapNewsUseCase
import com.mj.domain.usecase.GetNewsUseCase
import com.mj.domain.usecase.GetScrapNewsUseCase
import com.mj.presentation.home.model.NewsInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mj.domain.usecase.GetNewsUseCase.GetNewsParam as Param

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val getScrapNewsUseCase: GetScrapNewsUseCase,
    private val addScrapNewsUseCase: AddScrapNewsUseCase,
    private val deleteScrapNewsUseCase: DeleteScrapNewsUseCase,
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    companion object {
        private const val MAX_PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 10
    }

    private val _searchNewsState: MutableStateFlow<PagingData<NewsInfo.Content>> = MutableStateFlow(PagingData.empty())
    private val _scrapNewsState: MutableStateFlow<List<NewsInfo.Content>> = MutableStateFlow(emptyList())

    override fun setInitialState() = HomeContract.State(
        searchNewsPagingInfo = MutableStateFlow(PagingData.empty()),
        scrapNewsInfo = MutableStateFlow(emptyList()),
        isLoading = false,
        isError = false,
    )

    init {
        getScrapNews()
    }

    override fun handleEvents(event: HomeContract.Event) {
        Log.d("HomeViewModel", "event = $event")
        when (event) {
            is HomeContract.Event.NewsSelection -> setEffect { HomeContract.Effect.Navigation.ToDetail(event.url) }
            is HomeContract.Event.NewsScrap -> when (event.isAdd) {
                true -> addScrap(event.content)
                else -> deleteScrap(event.content)
            }
            is HomeContract.Event.SearchClick -> getSearchNews(event.query)
            is HomeContract.Event.Retry -> getSearchNews(event.query)
        }
    }

    private fun addScrap(content: NewsInfo.Content) {
        viewModelScope.launch {
            addScrapNewsUseCase(
                dispatcher = Dispatchers.IO,
                param = content.translate(),
            )
        }
    }

    private fun deleteScrap(content: NewsInfo.Content) {
        viewModelScope.launch {
            deleteScrapNewsUseCase(
                dispatcher = Dispatchers.IO,
                param = content.translate(),
            )
        }
    }

    private fun getSearchNews(query: String) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = MAX_PAGE_SIZE, prefetchDistance = PREFETCH_DISTANCE),
                pagingSourceFactory = {
                    SearchNewsPagingSource(
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
                    _searchNewsState.emit(it)
                    setState { copy(searchNewsPagingInfo = _searchNewsState, isLoading = false) }
                    setEffect { HomeContract.Effect.DataLoaded }
                }
        }
    }

    private inner class SearchNewsPagingSource(
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
    }

    private fun getScrapNews() {
        viewModelScope.launch {
            getScrapNewsUseCase()
                .distinctUntilChanged()
                .collect {
                    _scrapNewsState.emit(it.translate())
                    setState { copy(scrapNewsInfo = _scrapNewsState) }
                }
        }
    }

    private fun List<News.Content>.translate() = this.map {
        NewsInfo.Content(
            uid = it.uid,
            title = it.title,
            description = it.description,
            date = it.date,
            link = it.link,
        )
    }

    private fun News.translate(): NewsInfo =
        NewsInfo(
            currentPage = this.currentPage,
            contents = this.contents.map { content ->
                NewsInfo.Content(
                    uid = content.uid,
                    title = content.title,
                    description = content.description,
                    date = content.date,
                    link = content.link,
                )
            }
        )

    private fun NewsInfo.Content.translate() = News.Content(
        uid = uid,
        title = title,
        description = description,
        date = date,
        link = link,
    )
}



