package com.mj.compose_clean_architecture.ui.screen.detail

import android.util.Log
import com.mj.compose_clean_architecture.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor():
    BaseViewModel<DetailContract.Event, DetailContract.State, DetailContract.Effect>() {

    fun configure(url: String) {
        setState { copy(newsUrl = url) }
    }

    override fun setInitialState() = DetailContract.State(
        newsUrl = "",
    )

    override fun handleEvents(event: DetailContract.Event) {
        Log.d("DetailViewModel", "event = $event")
        when (event) {
            is DetailContract.Event.Back -> setEffect { DetailContract.Effect.Navigation.ToMain }
        }
    }
}