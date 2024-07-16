package com.mj.data.repo.remote.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mj.data.mapper.translate
import com.mj.data.repo.source.RemoteDataSource
import com.mj.domain.model.News

class NewsPagingSource(
    private val remoteDataSource: RemoteDataSource,
    private val query: String,
) : PagingSource<Int, News>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        return try {
            val currentPage = params.key ?: 1
            val news = remoteDataSource.getNewsData(
                query = query,
                page = currentPage,
            )

            LoadResult.Page(
                data = news.translate(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = news.start + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition
    }
}