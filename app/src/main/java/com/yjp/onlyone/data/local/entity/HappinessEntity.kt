package com.yjp.onlyone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "happiness")
data class HappinessEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    /**
     * 기록 기준 날짜. [java.time.LocalDate.toEpochDay] 값.
     * 오늘과 다르면 행복 지수는 초기화된 것으로 본다.
     */
    val recordDateEpochDay: Long,
    val mealCount: Int,
    val walkTotalMinutes: Int,
    val playTotalMinutes: Int,
    val snackCount: Int,
    val teethBrushed: Boolean,
) {
    companion object {
        const val SINGLETON_ID = 1
    }
}
