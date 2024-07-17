package com.mj.domain.model

data class News(
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