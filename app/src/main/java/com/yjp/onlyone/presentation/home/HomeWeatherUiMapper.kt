package com.yjp.onlyone.presentation.home

import androidx.annotation.DrawableRes
import com.yjp.onlyone.R
import com.yjp.onlyone.domain.model.HomeWeather
import com.yjp.onlyone.domain.model.WeatherSkySlot
import com.yjp.onlyone.util.KmaWeatherIconMapper
import com.yjp.onlyone.util.SunTimeUtil
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.math.roundToInt

object HomeWeatherUiMapper {

    fun map(
        weather: HomeWeather,
        latitude: Double,
        longitude: Double,
        now: LocalDateTime = LocalDateTime.now(),
    ): HomeWeatherUiState {
        return HomeWeatherUiState(
            currentTemperature = formatTemperature(weather.currentTemperatureCelsius),
            temperatureComparison = formatTemperatureComparison(
                todayAverage = weather.todayAverageCelsius,
                yesterdayAverage = weather.yesterdayAverageCelsius,
            ),
            weatherCondition = weather.weatherCondition,
            highLowTemperature = formatHighLow(
                min = weather.minTemperatureCelsius,
                max = weather.maxTemperatureCelsius,
            ),
            currentWeatherIconRes = iconRes(
                slot = weather.currentSky,
                latitude = latitude,
                longitude = longitude,
                now = now,
            ),
            hourlyForecasts = weather.hourlyForecasts.map { slot ->
                HomeWeatherHourlyUi(
                    timeLabel = formatHourlyTimeLabel(now, slot.hourOffset),
                    temperature = formatTemperature(slot.temperatureCelsius),
                    iconRes = iconRes(
                        slot = slot,
                        latitude = latitude,
                        longitude = longitude,
                        now = now,
                    ),
                )
            },
            isLoaded = true,
        )
    }

    @DrawableRes
    private fun iconRes(
        slot: WeatherSkySlot,
        latitude: Double,
        longitude: Double,
        now: LocalDateTime,
    ): Int {
        val targetTime = now.plusHours(slot.hourOffset.toLong())
        val isNight = SunTimeUtil.resolveSunPhase(
            latitude = latitude,
            longitude = longitude,
            now = targetTime,
        ).isNight

        return KmaWeatherIconMapper.iconRes(
            skyCode = slot.skyCode,
            precipitationCode = slot.precipitationCode,
            lightningValue = slot.lightningValue,
            isNight = isNight,
        )
    }

    fun formatTemperature(celsius: Float?): String {
        return celsius?.let { "${it.roundToInt()}°" } ?: "-"
    }

    fun formatHighLow(min: Float?, max: Float?): String {
        return "최고 ${formatTemperature(max)} 최저 ${formatTemperature(min)}"
    }

    fun formatTemperatureComparison(todayAverage: Float?, yesterdayAverage: Float?): String {
        if (todayAverage == null || yesterdayAverage == null) return "어제와 비교 정보 없음"
        val diff = (todayAverage - yesterdayAverage).roundToInt()
        return when {
            diff > 0 -> "어제보다 ${diff}° 높아요"
            diff < 0 -> "어제보다 ${abs(diff)}° 낮아요"
            else -> "어제와 같아요"
        }
    }

    fun formatHourlyTimeLabel(now: LocalDateTime, hourOffset: Int): String {
        val target = now.plusHours(hourOffset.toLong())
        val hour = target.hour
        val amPm = if (hour < 12) "오전" else "오후"
        val displayHour = when (hour % 12) {
            0 -> 12
            else -> hour % 12
        }
        return "$amPm ${displayHour}시"
    }
}

data class HomeWeatherUiState(
    val currentTemperature: String = "-",
    val temperatureComparison: String = "어제와 비교 정보 없음",
    val weatherCondition: String = "-",
    val highLowTemperature: String = "최고 - 최저 -",
    @DrawableRes val currentWeatherIconRes: Int = R.drawable.ic_sunny,
    val hourlyForecasts: List<HomeWeatherHourlyUi> = emptyList(),
    val isLoaded: Boolean = false,
)
