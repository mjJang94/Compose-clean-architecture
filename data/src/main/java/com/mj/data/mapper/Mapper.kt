package com.mj.data.mapper

import com.mj.data.repo.remote.data.NewsDto
import com.mj.domain.model.News

fun NewsDto.translate(): List<News> = this.items.map {
    News(
        title = it.title,
        description = it.description,
        date = it.pubDate,
        link = it.originallink,
    )
}