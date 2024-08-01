package com.mj.domain

import com.mj.domain.model.News
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getNews(query: String, currentPage: Int): News
    fun getScrapNews(): Flow<List<News.Content>>
    suspend fun deleteScrapNews(uid: Long)
    suspend fun addScrapNews(news: News.Content)
}