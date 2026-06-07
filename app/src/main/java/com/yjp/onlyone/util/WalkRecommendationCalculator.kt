package com.yjp.onlyone.util

import com.yjp.onlyone.domain.model.WeatherSkySlot
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 현재 시각을 포함해 이후 6시간(0~5시간 오프셋) 안에서
 * 기온·강수·눈·낙뢰 기준으로 산책하기 좋은 시각을 추천한다.
 */
object WalkRecommendationCalculator {

    const val LOOKAHEAD_HOUR_OFFSETS = 5
    private const val MIN_TEMPERATURE_C = -5f
    private const val MAX_TEMPERATURE_C = 28f
    /** 초단기실황 RN1(1시간 강수량, mm) — 이 이상이면 산책 비추천. */
    private const val HEAVY_RAIN_MM = 1f

    /** 빗방울(5)은 가벼운 강수로 허용, 그 외 강수·눈·진눈깨비·낙뢰는 비추천. */
    private val BLOCKED_PRECIPITATION_CODES = setOf("1", "2", "3", "4", "6", "7")

    sealed class Title {
        data class Range(
            val start: LocalDateTime,
            val end: LocalDateTime,
        ) : Title()

        data class After(
            val start: LocalDateTime,
        ) : Title()

        data class Before(
            val deadline: LocalDateTime,
        ) : Title()

        data object Difficult : Title()

        /** 6시간(0~5시간 오프셋) 전체가 산책 가능할 때. */
        data object AllHoursGood : Title()
    }

    fun recommend(
        slots: List<WeatherSkySlot>,
        now: LocalDateTime = LocalDateTime.now(),
    ): Title {
        val evaluations = (0..LOOKAHEAD_HOUR_OFFSETS).map { offset ->
            val slotTime = slotDateTime(now, offset)
            val slot = slots.firstOrNull { it.hourOffset == offset }
            WalkSlotEvaluation(
                offset = offset,
                time = slotTime,
                isWalkable = isWalkable(slot),
            )
        }

        val goodOffsets = evaluations.filter { it.isWalkable }.map { it.offset }
        if (goodOffsets.isEmpty()) return Title.Difficult

        if (evaluations.all { it.isWalkable }) {
            return Title.AllHoursGood
        }

        val primaryRange = contiguousRanges(goodOffsets).minBy { it.first }
        val startOffset = primaryRange.first
        val endOffset = primaryRange.last
        val badFollows = endOffset < LOOKAHEAD_HOUR_OFFSETS &&
            !evaluations[endOffset + 1].isWalkable

        return when {
            startOffset == endOffset && startOffset > 0 -> {
                Title.After(evaluations[startOffset].time)
            }

            startOffset == 0 && endOffset == 0 && badFollows -> {
                Title.Before(evaluations[endOffset + 1].time)
            }

            else -> {
                Title.Range(
                    start = evaluations[startOffset].time,
                    end = evaluations[endOffset].time,
                )
            }
        }
    }

    fun formatTitle(
        title: Title,
        now: LocalDateTime = LocalDateTime.now(),
    ): String {
        val today = now.toLocalDate()
        return when (title) {
            is Title.Range -> {
                val startDay = dayLabel(title.start.toLocalDate(), today)
                val endDay = dayLabel(title.end.toLocalDate(), today)
                val startTime = formatClockLabel(title.start)
                val endTime = formatClockLabel(title.end)
                if (startDay == endDay) {
                    "$startDay $startTime~$endTime 사이\n산책하기 좋아요!"
                } else {
                    "$startDay $startTime~$endDay $endTime 사이\n산책하기 좋아요!"
                }
            }

            is Title.After -> {
                val day = dayLabel(title.start.toLocalDate(), today)
                "${day} ${formatClockLabel(title.start)} 이후\n산책하기 좋아요!"
            }

            is Title.Before -> {
                val day = dayLabel(title.deadline.toLocalDate(), today)
                "${day} ${formatClockLabel(title.deadline)} 이전\n산책하기 좋아요!"
            }

            Title.Difficult -> "오늘은 산책하기 어려워요ㅠㅠ"

            Title.AllHoursGood -> "6시간 내에 모든 시간\n산책하기 좋아요!"
        }
    }

    fun isWalkable(slot: WeatherSkySlot?): Boolean {
        if (slot == null) return false

        val temperature = slot.temperatureCelsius ?: return false
        if (temperature < MIN_TEMPERATURE_C || temperature > MAX_TEMPERATURE_C) return false

        if (slot.lightningValue?.toFloatOrNull()?.let { it > 0f } == true) return false

        slot.precipitationMm?.let { rainfall ->
            if (rainfall >= HEAVY_RAIN_MM) return false
        }

        val precipitationCode = slot.precipitationCode
        if (precipitationCode in BLOCKED_PRECIPITATION_CODES) return false

        return true
    }

    private data class WalkSlotEvaluation(
        val offset: Int,
        val time: LocalDateTime,
        val isWalkable: Boolean,
    )

    /** offset 0은 정각이 아닌 실제 현재 시각으로 표시한다. */
    private fun slotDateTime(now: LocalDateTime, hourOffset: Int): LocalDateTime {
        if (hourOffset == 0) return now
        return now.plusHours(hourOffset.toLong())
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
    }

    private fun dayLabel(date: LocalDate, today: LocalDate): String {
        return when (date) {
            today -> "오늘"
            today.plusDays(1) -> "내일"
            else -> "${date.monthValue}월 ${date.dayOfMonth}일"
        }
    }

    fun formatClockLabel(time: LocalDateTime): String {
        return "${time.hour}시"
    }

    private fun contiguousRanges(offsets: List<Int>): List<IntRange> {
        if (offsets.isEmpty()) return emptyList()

        val sorted = offsets.sorted()
        val ranges = mutableListOf<IntRange>()
        var rangeStart = sorted.first()
        var previous = sorted.first()

        for (offset in sorted.drop(1)) {
            if (offset == previous + 1) {
                previous = offset
                continue
            }
            ranges += rangeStart..previous
            rangeStart = offset
            previous = offset
        }
        ranges += rangeStart..previous
        return ranges
    }
}
