package com.mj.domain

import com.mj.domain.model.News

interface Repository {
    suspend fun getNews(): List<News>
}