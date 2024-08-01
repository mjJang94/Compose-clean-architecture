package com.mj.data.mapper

import com.mj.data.repo.local.entity.NewsEntity
import com.mj.data.repo.remote.data.NewsDto
import com.mj.domain.model.News

fun NewsDto.translate(): News = News(
    currentPage = this.start,
    contents = this.items.map {
        News.Content(
            title = it.title,
            description = it.description,
            date = it.pubDate,
            link = it.originallink,
        )
    }
)

fun List<NewsEntity>.translate(): List<News.Content> = this.map {
    News.Content(
        uid = it.uid,
        title = it.title,
        description = it.description,
        date = it.date,
        link = it.link,
    )
}

fun News.Content.translate(): NewsEntity = NewsEntity(
    title = title,
    description = description,
    date = date,
    link = link,
)