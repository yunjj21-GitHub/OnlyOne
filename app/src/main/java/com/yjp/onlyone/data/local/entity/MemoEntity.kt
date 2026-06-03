package com.yjp.onlyone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val content: String,
) {
    companion object {
        const val SINGLETON_ID = 1
    }
}
