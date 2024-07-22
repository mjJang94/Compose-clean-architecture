package io.kiwiplus.app.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mj.compose_clean_architecture.model.NewsInfo
import com.mj.compose_clean_architecture.ui.base.BaseViewModel
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
                        getNewsUseCase = getNewsUseCase,
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



