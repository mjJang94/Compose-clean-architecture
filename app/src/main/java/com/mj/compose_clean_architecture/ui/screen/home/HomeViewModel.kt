package com.mj.compose_clean_architecture.ui.screen.home

import android.util.Log
import com.mj.compose_clean_architecture.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    init {
        //TODO getDefaultNews()
    }

    override fun setInitialState() = HomeContract.State(
        news = emptyList(),
        isLoading = false,
        isError = false,
    )

    override fun handleEvents(event: HomeContract.Event) {
        Log.d("HomeViewModel", "event = $event")
        when (event) {
            is HomeContract.Event.NewsSelection -> setEffect { HomeContract.Effect.Navigation.ToDetail(event.news) }
            //TODO getNews()
            is HomeContract.Event.SearchClick -> Unit
            is HomeContract.Event.Retry -> Unit
        }
    }
}