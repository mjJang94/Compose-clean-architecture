package com.mj.presentation.detail

import com.mj.core.base.ViewEvent
import com.mj.core.base.ViewSideEffect
import com.mj.core.base.ViewState

class DetailContract {

    sealed class Event: ViewEvent {
        data object Back: Event()
        data class Loading(val progress: Int): Event()
    }

    data class State(
        val newsUrl: String,
        val progress: Int,
    ): ViewState

    sealed class Effect: ViewSideEffect {

        sealed class Navigation: Effect() {
            data object ToMain: Navigation()
        }
    }
}