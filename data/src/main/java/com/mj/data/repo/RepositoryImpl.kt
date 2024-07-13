package com.mj.data.repo

import com.mj.data.mapper.translate
import com.mj.data.repo.remote.api.NaverApi
import com.mj.domain.Repository
import com.mj.domain.model.News
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val naverApi: NaverApi
) : Repository {

    override suspend fun getNews(): List<News> =
        naverApi.getNews().translate()

}