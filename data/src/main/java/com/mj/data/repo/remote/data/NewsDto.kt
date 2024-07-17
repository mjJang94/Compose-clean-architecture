package com.mj.data.repo.remote.data


data class NewsDto(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<Contents>,
) {
    data class Contents(
        val title: String,
        val originallink: String,
        val description: String,
        val pubDate: String,
    )
}
