package com.mj.compose_clean_architecture.model

data class NewsInfo(
    val currentPage: Int,
    val contents: List<Content>,
){
    data class Content(
        val title: String,
        val description: String,
        val date: String,
        val link: String,
    )
}