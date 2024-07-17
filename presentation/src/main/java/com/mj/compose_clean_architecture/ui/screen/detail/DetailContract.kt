package com.mj.compose_clean_architecture.ui.screen.detail

import com.mj.compose_clean_architecture.ui.base.ViewEvent
import com.mj.compose_clean_architecture.ui.base.ViewSideEffect
import com.mj.compose_clean_architecture.ui.base.ViewState

class DetailContract {

    sealed class Event: ViewEvent {
        data object Back: Event()
    }

    data class State(
        val newsUrl: String,
    ): ViewState

    sealed class Effect: ViewSideEffect {

        sealed class Navigation: Effect() {
            data object ToMain: Navigation()
        }
    }
}