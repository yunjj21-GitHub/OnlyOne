package com.yjp.onlyone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yjp.onlyone.data.local.dao.HappinessDao
import com.yjp.onlyone.data.local.dao.MemoDao
import com.yjp.onlyone.data.local.dao.PetDao
import com.yjp.onlyone.data.local.entity.HappinessEntity
import com.yjp.onlyone.data.local.entity.MemoEntity
import com.yjp.onlyone.data.local.entity.PetEntity

@Database(
    entities = [MemoEntity::class, PetEntity::class, HappinessEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class OnlyOneDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
    abstract fun petDao(): PetDao
    abstract fun happinessDao(): HappinessDao
}
