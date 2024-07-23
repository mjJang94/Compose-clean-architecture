package com.mj.feature.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mj.domain.model.News
import com.mj.domain.usecase.GetNewsUseCase
import com.mj.feature.home.model.NewsInfo

class NewsPagingSource(
    private val getNewsUseCase: GetNewsUseCase,
    private val query: String,
) : PagingSource<Int, NewsInfo.Content>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsInfo.Content> {
        return try {
            val currentPage = params.key ?: 1
            val news = getNewsUseCase(
                query = query,
                start = currentPage,
            ).translate()

            LoadResult.Page(
                data = news.contents,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = news.currentPage + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsInfo.Content>): Int? {
        return state.anchorPosition
    }

    private fun News.translate(): NewsInfo =
        NewsInfo(
            currentPage = this.currentPage,
            contents = this.contents.map { content ->
                NewsInfo.Content(
                    title = content.title,
                    description = content.description,
                    date = content.date,
                    link = content.link,
                )
            }
        )
}

