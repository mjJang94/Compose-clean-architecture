package com.mj.data.repo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mj.data.repo.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM newsentity")
    fun flow(): Flow<List<NewsEntity>>

    @Query("DELETE FROM newsentity WHERE uid=:uid")
    suspend fun deleteById(uid: Long): Int

    @Insert
    suspend fun insert(newsEntity: NewsEntity)
}