package com.yjp.onlyone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yjp.onlyone.data.local.entity.PetEntity

@Dao
interface PetDao {

    @Query("SELECT * FROM pet WHERE id = :id LIMIT 1")
    suspend fun get(id: Int = PetEntity.SINGLETON_ID): PetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(pet: PetEntity)
}
