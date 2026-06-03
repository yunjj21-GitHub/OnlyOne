package com.yjp.onlyone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yjp.onlyone.data.local.entity.MemoEntity

@Dao
interface MemoDao {

    @Query("SELECT content FROM memo WHERE id = :id LIMIT 1")
    suspend fun getContent(id: Int = MemoEntity.SINGLETON_ID): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(memo: MemoEntity)
}
