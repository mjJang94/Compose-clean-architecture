package com.mj.data.repo.remote.data


data class NewsDto(
    val start: Int,
    val items: List<Contents>,
) {
    data class Contents(
        val title: String,
        val originallink: String,
        val description: String,
        val pubDate: String,
    )
}
