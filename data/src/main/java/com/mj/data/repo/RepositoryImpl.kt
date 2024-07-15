package com.mj.data.repo

import com.mj.data.mapper.translate
import com.mj.data.repo.source.RemoteDataSource
import com.mj.domain.Repository

class RepositoryImpl(private val remoteDataSource: RemoteDataSource) : Repository {

    override suspend fun getNews(query: String) =
        remoteDataSource.getNewsData(query = query, page = 1).translate()

}