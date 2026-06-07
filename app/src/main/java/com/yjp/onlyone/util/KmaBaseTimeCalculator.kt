package com.yjp.onlyone.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/** 기상청 API base_date / base_time 계산. 항상 디바이스 현재 시각을 기준으로 한다. */
object KmaBaseTimeCalculator {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
    private val hourFormatter = DateTimeFormatter.ofPattern("HH00")
    private val halfHourFormatter = DateTimeFormatter.ofPattern("HH30")

    /** 초단기실황 API가 제공하는 과거 자료 보관 시간. */
    private const val NOWCAST_RETENTION_HOURS = 24L

    /** 단기예보 base_time 발표 시각(시). API 스펙 고정값. */
    private val vilageIssueHours = listOf(2, 5, 8, 11, 14, 17, 20, 23)

    data class BaseDateTime(
        val date: String,
        val time: String,
    ) {
        fun label(): String = "$date $time"

        fun toLocalDateTime(): LocalDateTime? {
            return runCatching {
                LocalDateTime.parse(date + time, dateTimeFormatter)
            }.getOrNull()
        }
    }

    data class PastNowcastCandidate(
        val base: BaseDateTime,
        val hoursAgo: Long,
    )

    /** 초단기실황 — 정시(HH00) 기준, 해당 시각 +10분 이후 조회 가능. */
    fun nowcast(now: LocalDateTime = LocalDateTime.now()): BaseDateTime {
        val base = now.withMinute(0).withSecond(0).withNano(0)
            .let { hourStart -> if (now.minute < 10) hourStart.minusHours(1) else hourStart }
        return BaseDateTime(
            date = base.format(dateFormatter),
            time = base.format(hourFormatter),
        )
    }

    /** 초단기예보 — 30분(HH30) 기준, 해당 시각 +45분 이후 조회 가능. */
    fun ultraShortForecast(now: LocalDateTime = LocalDateTime.now()): BaseDateTime {
        val base = now.withMinute(30).withSecond(0).withNano(0)
            .let { halfHour -> if (now.minute < 45) halfHour.minusHours(1) else halfHour }
        return BaseDateTime(
            date = base.format(dateFormatter),
            time = base.format(halfHourFormatter),
        )
    }

    /** 단기예보 — 발표 시각 +10분 이후 가장 최근 슬롯. */
    fun vilageForecast(now: LocalDateTime = LocalDateTime.now()): BaseDateTime {
        val matchedHour = vilageIssueHours.asReversed().firstOrNull { hour ->
            val availableFrom = now.withHour(hour).withMinute(10).withSecond(0).withNano(0)
            !now.isBefore(availableFrom)
        }

        val base = if (matchedHour != null) {
            now.withHour(matchedHour).withMinute(0).withSecond(0).withNano(0)
        } else {
            now.minusDays(1).withHour(vilageIssueHours.last()).withMinute(0).withSecond(0).withNano(0)
        }

        return BaseDateTime(
            date = base.format(dateFormatter),
            time = base.format(hourFormatter),
        )
    }

    fun todayDate(now: LocalDateTime = LocalDateTime.now()): String {
        return now.format(dateFormatter)
    }

    /**
     * 금일 TMN/TMX 보완용 0200 발표 슬롯.
     * 최신 슬롯(17·20·23시)에는 금일 TMN/TMX가 빠지는 경우가 많다.
     */
    fun vilageForecast0200Today(now: LocalDateTime = LocalDateTime.now()): BaseDateTime? {
        val base = now.withHour(2).withMinute(0).withSecond(0).withNano(0)
        val availableFrom = base.plusMinutes(10)
        return if (!now.isBefore(availableFrom)) {
            BaseDateTime(
                date = base.format(dateFormatter),
                time = base.format(hourFormatter),
            )
        } else {
            null
        }
    }

    /** 02:10 이전 금일 TMN 조회용 전일 2300 발표 슬롯. */
    fun vilageForecastPreviousDayLast(now: LocalDateTime = LocalDateTime.now()): BaseDateTime {
        val base = now.minusDays(1)
            .withHour(vilageIssueHours.last())
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        return BaseDateTime(
            date = base.format(dateFormatter),
            time = base.format(hourFormatter),
        )
    }

    fun forecastSlot(now: LocalDateTime, hourOffset: Int): BaseDateTime {
        val target = now.plusHours(hourOffset.toLong())
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        return BaseDateTime(
            date = target.format(dateFormatter),
            time = target.format(hourFormatter),
        )
    }

    /**
     * 어제 동시각 비교용 초단기실황 슬롯.
     * API는 최근 24시간 자료만 제공하므로, 24시간 경계를 넘지 않는 가장 오래된 유효 슬롯을 고른다.
     */
    fun pastNowcastForComparison(now: LocalDateTime = LocalDateTime.now()): PastNowcastCandidate? {
        return pastNowcastCandidates(now).firstOrNull()
    }

    /** 어제 비교용으로 시도할 수 있는 과거 실측 슬롯 목록 (최근 것부터). */
    fun pastNowcastCandidates(now: LocalDateTime = LocalDateTime.now()): List<PastNowcastCandidate> {
        val maxLookbackHours = maxNowcastLookbackHours(now)
        return (maxLookbackHours downTo 1L).mapNotNull { hoursAgo ->
            val base = nowcast(now.minusHours(hoursAgo))
            if (isNowcastBaseAvailable(now, base)) {
                PastNowcastCandidate(base = base, hoursAgo = hoursAgo)
            } else {
                null
            }
        }
    }

    /** 초단기실황 base_time이 요청 시각 기준 API 제공 범위 안인지 확인한다. */
    fun isNowcastBaseAvailable(now: LocalDateTime, base: BaseDateTime): Boolean {
        val baseMoment = base.toLocalDateTime() ?: return false
        if (!baseMoment.isBefore(now)) return false

        val oldestAvailable = now.minusHours(NOWCAST_RETENTION_HOURS).plusMinutes(1)
        return !baseMoment.isBefore(oldestAvailable)
    }

    private fun maxNowcastLookbackHours(now: LocalDateTime): Long {
        val oldestAvailable = now.minusHours(NOWCAST_RETENTION_HOURS).plusMinutes(1)
        return ChronoUnit.HOURS.between(oldestAvailable, now)
            .coerceAtLeast(1L)
    }
}
