package com.mj.data.repo.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val title: String,
    val description: String,
    val date: String,
    val link: String,
)
