package com.mj.domain

import androidx.paging.PagingData
import com.mj.domain.model.News
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getNews(query: String): Flow<PagingData<News>>
}