package com.mj.data.repo.source

import com.mj.data.repo.remote.data.NewsDto

interface RemoteDataSource {
    suspend fun getNewsData(query: String, page: Int, pageSize: Int = 20): List<NewsDto>
}