package com.mj.data.repo

import com.mj.data.mapper.translate
import com.mj.data.repo.local.dao.NewsDao
import com.mj.data.repo.source.RemoteDataSource
import com.mj.domain.Repository
import com.mj.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val newsDao: NewsDao,
) : Repository {

    override suspend fun getNews(query: String, currentPage: Int): News =
        remoteDataSource.getNewsData(query = query, page = currentPage).translate()

    override fun getScrapNews(): Flow<List<News.Content>> =
        newsDao.flow().map { it.translate() }

    override suspend fun getScrapNewsByData(title: String, date: String): News.Content? =
        newsDao.getByData(title, date)?.translate()

    override suspend fun deleteScrapNews(uid: Long) {
        newsDao.deleteById(uid)
    }

    override suspend fun addScrapNews(news: News.Content) {
        newsDao.insert(news.translate())
    }
}