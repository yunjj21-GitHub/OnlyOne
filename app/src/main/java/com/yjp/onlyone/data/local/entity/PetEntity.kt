package com.yjp.onlyone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet")
data class PetEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val iconRes: Int,
    val name: String,
    /**
     * 입양 날짜. [java.time.LocalDate.toEpochDay] 값(1970-01-01 기준 일 수).
     * 시각·타임존 없이 날짜만 저장.
     * 예: 2019-02-04 → 17931
     */
    val adoptionDateEpochDay: Long,
) {
    companion object {
        const val SINGLETON_ID = 1
    }
}
