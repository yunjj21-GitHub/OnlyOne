package com.yjp.onlyone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yjp.onlyone.data.local.dao.MemoDao
import com.yjp.onlyone.data.local.entity.MemoEntity

@Database(
    entities = [MemoEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class OnlyOneDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}
