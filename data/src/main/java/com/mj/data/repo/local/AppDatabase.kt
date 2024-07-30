package com.mj.data.repo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mj.data.repo.local.dao.NewsDao
import com.mj.data.repo.local.entity.NewsEntity


@Database(
    entities = [
        NewsEntity::class,
    ],
    version = 1,
    exportSchema = false,
    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}