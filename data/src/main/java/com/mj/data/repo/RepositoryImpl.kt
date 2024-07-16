package com.mj.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mj.data.repo.remote.data.NewsPagingSource
import com.mj.data.repo.source.RemoteDataSource
import com.mj.domain.Repository
import com.mj.domain.model.News
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override val maxPageSize: Int = 20
    override val prefetchDistance: Int = 8

    override suspend fun getNews(query: String): Flow<PagingData<News>> {
     val s = Pager(
         config = PagingConfig(pageSize = maxPageSize, prefetchDistance),
         pagingSourceFactory = {
             NewsPagingSource(
                 remoteDataSource = remoteDataSource,
                 query = query,
             )
         }
     ).flow

     return s
    }
}