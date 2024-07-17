package com.mj.domain

import com.mj.domain.model.News

interface Repository {
    suspend fun getNews(query: String, currentPage: Int): News
}