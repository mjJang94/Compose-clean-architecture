package com.mj.data.mapper

import com.mj.data.repo.remote.data.NewsDto
import com.mj.domain.model.News

fun List<NewsDto>.translate(): List<News> = this.map {
    News(
        title = it.title,
        description = it.description,
        date = it.pubDate,
        link = it.originallink,
    )
}