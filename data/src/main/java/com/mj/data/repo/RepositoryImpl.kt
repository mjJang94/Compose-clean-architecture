package com.mj.data.repo

import com.mj.data.mapper.translate
import com.mj.data.repo.source.RemoteDataSource
import com.mj.domain.Repository
import com.mj.domain.model.News
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getNews(query: String, currentPage: Int): News =
        remoteDataSource.getNewsData(query = query, page = currentPage).translate()
}