package com.mj.data.repo.remote.api

import com.mj.data.repo.remote.data.NewsDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverApi {
    @GET(Endpoints.GET_NEWS)
    suspend fun getNews(
        @Header("X-Naver-Client-Id") id: String = Endpoints.NAVER_CLIENT_ID,
        @Header("X-Naver-Client-Secret") secret: String = Endpoints.NAVER_CLIENT_SECRET,
        @Query("start") start: Int = 1,
        @Query("display") display: Int,
        @Query("query") q: String,
    ): NewsDto
}