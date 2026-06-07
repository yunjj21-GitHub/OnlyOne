package com.yjp.onlyone.util

import com.yjp.onlyone.domain.model.WeatherSkySlot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class WalkRecommendationCalculatorTest {

    @Test
    fun `날씨가 나쁘면 어려워요 문구를 반환한다`() {
        val now = LocalDateTime.of(2026, 6, 6, 14, 0)
        val slots = walkableSlots(now).map { it.copy(precipitationCode = "1") }

        val title = WalkRecommendationCalculator.recommend(slots = slots, now = now)

        assertTrue(title is WalkRecommendationCalculator.Title.Difficult)
        assertEquals(
            "오늘은 산책하기 어려워요ㅠㅠ",
            WalkRecommendationCalculator.formatTitle(title, now),
        )
    }

    @Test
    fun `6시간 전체가 좋으면 모든 시간 문구를 반환한다`() {
        val now = LocalDateTime.of(2026, 6, 6, 14, 0)
        val slots = walkableSlots(now)

        val title = WalkRecommendationCalculator.recommend(slots = slots, now = now)

        assertTrue(title is WalkRecommendationCalculator.Title.AllHoursGood)
        assertEquals(
            "6시간 내에 모든 시간\n산책하기 좋아요!",
            WalkRecommendationCalculator.formatTitle(title, now),
        )
    }

    @Test
    fun `일부 시간만 좋으면 구간 문구를 반환한다`() {
        val now = LocalDateTime.of(2026, 6, 6, 14, 0)
        val slots = walkableSlots(now).map { slot ->
            if (slot.hourOffset == 5) slot.copy(precipitationCode = "1") else slot
        }

        val title = WalkRecommendationCalculator.recommend(slots = slots, now = now)

        assertTrue(title is WalkRecommendationCalculator.Title.Range)
        val formatted = WalkRecommendationCalculator.formatTitle(title, now)
        assertTrue(formatted.contains("14시"))
        assertTrue(formatted.contains("사이"))
        assertTrue(formatted.contains("산책하기 좋아요"))
    }

    @Test
    fun `현재 시각부터 6시간 전체가 좋으면 모든 시간 문구를 반환한다`() {
        val now = LocalDateTime.of(2026, 6, 6, 19, 55)
        val slots = walkableSlots(now)

        val title = WalkRecommendationCalculator.recommend(slots = slots, now = now)

        assertTrue(title is WalkRecommendationCalculator.Title.AllHoursGood)
        assertEquals(
            "6시간 내에 모든 시간\n산책하기 좋아요!",
            WalkRecommendationCalculator.formatTitle(title, now),
        )
    }

    @Test
    fun `이후에만 좋은 시간이 있으면 이후 문구를 반환한다`() {
        val now = LocalDateTime.of(2026, 6, 6, 20, 15)
        val slots = walkableSlots(now).map { slot ->
            when (slot.hourOffset) {
                0, 2, 3, 4, 5 -> slot.copy(precipitationCode = "1")
                else -> slot
            }
        }

        val title = WalkRecommendationCalculator.recommend(slots = slots, now = now)

        assertTrue(title is WalkRecommendationCalculator.Title.After)
        val formatted = WalkRecommendationCalculator.formatTitle(title, now)
        assertTrue(formatted.contains("이후"))
        assertTrue(formatted.contains("산책하기 좋아요"))
    }

    @Test
    fun `현재만 좋고 바로 악화되면 이전 문구를 반환한다`() {
        val now = LocalDateTime.of(2026, 6, 6, 20, 15)
        val slots = walkableSlots(now).map { slot ->
            if (slot.hourOffset == 1) {
                slot.copy(precipitationCode = "1")
            } else {
                slot
            }
        }

        val title = WalkRecommendationCalculator.recommend(slots = slots, now = now)

        assertTrue(title is WalkRecommendationCalculator.Title.Before)
        val formatted = WalkRecommendationCalculator.formatTitle(title, now)
        assertTrue(formatted.contains("이전"))
        assertTrue(formatted.contains("산책하기 좋아요"))
    }

    @Test
    fun `너무 춥거나 더우면 산책 비추천한다`() {
        val now = LocalDateTime.of(2026, 6, 6, 20, 15)
        val coldSlots = walkableSlots(now).map { it.copy(temperatureCelsius = -10f) }
        val hotSlots = walkableSlots(now).map { it.copy(temperatureCelsius = 32f) }

        assertTrue(
            WalkRecommendationCalculator.recommend(coldSlots, now)
                is WalkRecommendationCalculator.Title.Difficult,
        )
        assertTrue(
            WalkRecommendationCalculator.recommend(hotSlots, now)
                is WalkRecommendationCalculator.Title.Difficult,
        )
    }

    private fun walkableSlots(now: LocalDateTime): List<WeatherSkySlot> {
        return (0..WalkRecommendationCalculator.LOOKAHEAD_HOUR_OFFSETS).map { offset ->
            val slotTime = now.plusHours(offset.toLong())
            WeatherSkySlot(
                hourOffset = offset,
                temperatureCelsius = 18f,
                skyCode = "1",
                precipitationCode = "0",
                lightningValue = "0",
                precipitationMm = 0f,
                forecastDate = slotTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")),
                forecastTime = slotTime.format(java.time.format.DateTimeFormatter.ofPattern("HH00")),
            )
        }
    }
}
