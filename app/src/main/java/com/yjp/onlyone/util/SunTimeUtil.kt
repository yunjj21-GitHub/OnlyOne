package com.yjp.onlyone.util

import org.shredzone.commons.suncalc.SunTimes
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** 위·경도 기준 일출/일몰로 낮·밤을 판별한다. */
object SunTimeUtil {

    private val KOREA_ZONE: ZoneId = ZoneId.of("Asia/Seoul")
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    data class SunPhase(
        val isNight: Boolean,
        val sunriseTime: String,
        val sunsetTime: String,
    )

    fun resolveSunPhase(
        latitude: Double,
        longitude: Double,
        now: LocalDateTime = LocalDateTime.now(),
    ): SunPhase {
        val zonedNow = now.atZone(KOREA_ZONE)
        val sunTimes = SunTimes.compute()
            .on(zonedNow.toLocalDate())
            .at(latitude, longitude)
            .timezone(KOREA_ZONE)
            .execute()

        val sunrise = sunTimes.rise?.toInstant()?.atZone(KOREA_ZONE)
        val sunset = sunTimes.set?.toInstant()?.atZone(KOREA_ZONE)

        if (sunrise == null || sunset == null) {
            return SunPhase(
                isNight = isNightByHourFallback(now),
                sunriseTime = "-",
                sunsetTime = "-",
            )
        }

        val isNight = zonedNow.isBefore(sunrise) || zonedNow.isAfter(sunset)
        return SunPhase(
            isNight = isNight,
            sunriseTime = sunrise.format(timeFormatter),
            sunsetTime = sunset.format(timeFormatter),
        )
    }

    /** 일출/일몰 계산 실패 시 사용하는 고정 시간대 (20시~06시). */
    private fun isNightByHourFallback(now: LocalDateTime): Boolean {
        val hour = now.hour
        return hour < 6 || hour >= 20
    }
}
