package com.mj.data.repo.source

import com.mj.data.repo.remote.api.NaverApi
import com.mj.data.repo.remote.data.NewsDto
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: NaverApi
): RemoteDataSource {
    override suspend fun getNewsData(query: String, page: Int, pageSize: Int): NewsDto {
        return apiService.getNews(q = query, start = page, display = pageSize)
    }
}