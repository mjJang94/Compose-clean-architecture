package com.mj.presentation.home.model

data class NewsInfo(
    val currentPage: Int,
    val contents: List<Content>,
){
    data class Content(
        val uid: Long,
        val title: String,
        val description: String,
        val date: String,
        val link: String,
    )
}