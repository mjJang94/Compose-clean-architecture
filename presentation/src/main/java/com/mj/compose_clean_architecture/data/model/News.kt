package com.mj.compose_clean_architecture.data.model

import com.google.gson.annotations.SerializedName

data class News(
    val title: String,
    val description: String,
    @SerializedName("pubDate")
    val date: String,
    @SerializedName("originallink")
    val link: String,
)
