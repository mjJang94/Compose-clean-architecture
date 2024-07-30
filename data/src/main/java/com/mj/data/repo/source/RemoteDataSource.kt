package com.mj.data.repo.source

import com.mj.data.repo.local.entity.NewsEntity
import com.mj.data.repo.remote.data.NewsDto
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    //local
    suspend fun insertNews(newsEntity: NewsEntity)
    suspend fun deleteNews(uid: Long): Int
    fun newsFlow(): Flow<List<NewsEntity>>

    //remote
    suspend fun getNewsData(query: String, page: Int, pageSize: Int = 20): NewsDto
}