package com.mj.data.repo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mj.data.repo.local.entity.NewsEntity
import com.mj.domain.model.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM newsentity")
    fun flow(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM newsentity WHERE title = :title OR date = :date LIMIT 1")
    suspend fun getByData(title: String, date:String): NewsEntity?

    @Query("DELETE FROM newsentity WHERE uid=:uid")
    suspend fun deleteById(uid: Long): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newsEntity: NewsEntity)
}