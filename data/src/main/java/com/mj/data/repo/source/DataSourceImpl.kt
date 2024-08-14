package com.mj.data.repo.source

import com.mj.data.repo.local.dao.NewsDao
import com.mj.data.repo.local.entity.NewsEntity
import com.mj.data.repo.remote.api.NaverApi
import com.mj.data.repo.remote.data.NewsDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataSourceImpl @Inject constructor(
    private val naverRemote: NaverApi,
    private val newsDao: NewsDao,
) : DataSource {
    override suspend fun insertNews(newsEntity: NewsEntity) =
        newsDao.insert(newsEntity)

    override suspend fun deleteNews(uid: Long) =
        newsDao.deleteById(uid)

    override fun newsFlow(): Flow<List<NewsEntity>> =
        newsDao.flow()

    override suspend fun getNewsByData(title: String, date: String): NewsEntity? =
        newsDao.getByData(title, date)

    override suspend fun getNewsData(query: String, page: Int, pageSize: Int): NewsDto =
        naverRemote.getNews(q = query, start = page, display = pageSize)
}