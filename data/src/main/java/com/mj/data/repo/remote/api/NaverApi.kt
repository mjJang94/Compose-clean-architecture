package com.mj.data.repo.remote.api

import com.mj.data.repo.remote.Endpoints
import com.mj.data.repo.remote.data.NewsDto
import retrofit2.http.GET

interface NaverApi {
    @GET(Endpoints.GET_NEWS)
    suspend fun getNews(): List<NewsDto>
}