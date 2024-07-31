package com.mj.domain

import com.mj.domain.model.News
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getNews(query: String, currentPage: Int): News
    suspend fun getScrapNews(): Flow<News.Content>
    suspend fun deleteScrapNews(news: News.Content)
    suspend fun addScrapNews(news: News.Content)
}