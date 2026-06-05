package com.yjp.onlyone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yjp.onlyone.data.local.entity.HappinessEntity

@Dao
interface HappinessDao {

    @Query("SELECT * FROM happiness WHERE id = :id LIMIT 1")
    suspend fun get(id: Int = HappinessEntity.SINGLETON_ID): HappinessEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(happiness: HappinessEntity)
}
