@file:SuppressLint("NewApi")

package com.yjp.onlyone.util

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private val UtcTimeZone: TimeZone = TimeZone.getTimeZone("UTC")

@OptIn(ExperimentalMaterial3Api::class)
object PastOrTodaySelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= todayUtcPickerMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= Calendar.getInstance().get(Calendar.YEAR)
    }
}

fun localDateOf(year: Int, month: Int, dayOfMonth: Int): LocalDate {
    return LocalDate.of(year, month, dayOfMonth)
}

/** [fromDate]부터 [toDate]까지 경과 일 수(당일=0). 기본 [toDate]는 기기 오늘. */
fun daysFromToToday(
    fromDate: LocalDate,
    toDate: LocalDate = todayLocalDate(),
): Int {
    return ChronoUnit.DAYS.between(fromDate, toDate).toInt().coerceAtLeast(0)
}

fun todayLocalDate(): LocalDate {
    val today = Calendar.getInstance()
    return localDateOf(
        today.get(Calendar.YEAR),
        today.get(Calendar.MONTH) + 1,
        today.get(Calendar.DAY_OF_MONTH),
    )
}

fun LocalDate.toEpochDayValue(): Long = toEpochDay()

fun Long.toLocalDateFromEpochDay(): LocalDate = LocalDate.ofEpochDay(this)

fun LocalDate.toUtcPickerMillis(): Long {
    return yearMonthDayToUtcPickerMillis(year, monthValue, dayOfMonth)
}

fun Long.toLocalDateFromUtcPickerMillis(): LocalDate {
    val (year, month, dayOfMonth) = utcPickerMillisToYearMonthDay(this)
    return LocalDate.of(year, month, dayOfMonth)
}

fun LocalDate.formatDotSeparated(): String {
    return String.format(
        Locale.getDefault(),
        "%04d.%02d.%02d",
        year,
        monthValue,
        dayOfMonth,
    )
}

private fun yearMonthDayToUtcPickerMillis(year: Int, month: Int, dayOfMonth: Int): Long {
    return Calendar.getInstance(UtcTimeZone).apply {
        clear()
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, dayOfMonth)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun utcPickerMillisToYearMonthDay(utcTimeMillis: Long): Triple<Int, Int, Int> {
    val calendar = Calendar.getInstance(UtcTimeZone).apply {
        timeInMillis = utcTimeMillis
    }
    return Triple(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH),
    )
}

private fun todayUtcPickerMillis(): Long {
    val today = Calendar.getInstance()
    return yearMonthDayToUtcPickerMillis(
        today.get(Calendar.YEAR),
        today.get(Calendar.MONTH) + 1,
        today.get(Calendar.DAY_OF_MONTH),
    )
}
