package com.mj.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mj.data.mapper.translate
import com.mj.data.repo.source.RemoteDataSource
import com.mj.domain.Repository
import com.mj.domain.model.News
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getNews(query: String): Flow<PagingData<News>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ItemPagingSource(remoteDataSource, query) }
        ).flow

    class ItemPagingSource(
        private val dataSourceImpl: RemoteDataSource,
        private val query: String,
    ) : PagingSource<Int, News>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
            return try {
                val page = params.key ?: 1
                val response = dataSourceImpl.getNewsData(query, page).translate()
                LoadResult.Page(
                    data = response,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (response.isEmpty()) null else page + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, News>): Int? {
            // 가장 최근에 접근한 페이지의 키를 반환
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }
    }
}