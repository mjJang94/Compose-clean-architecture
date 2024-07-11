package com.mj.compose_clean_architecture.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()
    fun updateQuery(input: String) {
        viewModelScope.launch {
            _query.emit(input)
        }
    }
}