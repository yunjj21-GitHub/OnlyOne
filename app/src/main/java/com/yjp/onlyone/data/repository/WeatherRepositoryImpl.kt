package com.yjp.onlyone.data.repository

import com.yjp.onlyone.data.remote.api.KmaWeatherApiService
import com.yjp.onlyone.data.remote.dto.KmaItemDto
import com.yjp.onlyone.data.remote.dto.dailyTemperature
import com.yjp.onlyone.data.remote.dto.itemsOrEmpty
import com.yjp.onlyone.data.remote.dto.observationValue
import com.yjp.onlyone.data.remote.dto.resolveDailyMinMax
import com.yjp.onlyone.domain.model.HomeWeather
import com.yjp.onlyone.domain.model.WeatherSkySlot
import com.yjp.onlyone.domain.repository.WeatherRepository
import com.yjp.onlyone.util.KmaBaseTimeCalculator
import com.yjp.onlyone.util.KmaWeatherIconMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherRepositoryImpl @Inject constructor(
    private val api: KmaWeatherApiService,
) : WeatherRepository {

    override suspend fun fetchHomeWeather(
        nx: Int,
        ny: Int,
        latitude: Double,
        longitude: Double,
    ): HomeWeather = withContext(Dispatchers.IO) {
        val now = LocalDateTime.now()
        val today = KmaBaseTimeCalculator.todayDate(now)
        val ncstBase = KmaBaseTimeCalculator.nowcast(now)
        val ultraBase = KmaBaseTimeCalculator.ultraShortForecast(now)
        val vilageBase = KmaBaseTimeCalculator.vilageForecast(now)

        val ncst = api.getUltraShortNowcast(ncstBase.date, ncstBase.time, nx, ny).itemsOrEmpty()
        val ultra = api.getUltraShortForecast(ultraBase.date, ultraBase.time, nx, ny).itemsOrEmpty()
        val vilage = api.getVilageForecast(vilageBase.date, vilageBase.time, nx, ny).itemsOrEmpty()

        val (minCelsius, maxCelsius) = resolveTodayDailyTemperatures(
            today = today,
            now = now,
            latestBase = vilageBase,
            latestItems = vilage,
            nx = nx,
            ny = ny,
        )
        val todayAverage = averageCelsius(minCelsius, maxCelsius)
        val yesterdayAverage = fetchYesterdayAverageCelsius(now, nx, ny)
        val currentSky = resolveSkySlot(
            hourOffset = 0,
            now = now,
            ncst = ncst,
            ultra = ultra,
        )
        val hourlyForecasts = (0..3).map { offset ->
            resolveSkySlot(
                hourOffset = offset,
                now = now,
                ncst = ncst,
                ultra = ultra,
            )
        }

        HomeWeather(
            currentTemperatureCelsius = ncst.observationValue("T1H")?.toFloatOrNull()
                ?: currentSky.temperatureCelsius,
            weatherCondition = KmaWeatherIconMapper.weatherLabel(
                skyCode = currentSky.skyCode,
                precipitationCode = currentSky.precipitationCode,
                lightningValue = currentSky.lightningValue,
            ),
            minTemperatureCelsius = minCelsius,
            maxTemperatureCelsius = maxCelsius,
            todayAverageCelsius = todayAverage,
            yesterdayAverageCelsius = yesterdayAverage,
            currentSky = currentSky,
            hourlyForecasts = hourlyForecasts,
        )
    }

    private fun resolveSkySlot(
        hourOffset: Int,
        now: LocalDateTime,
        ncst: List<KmaItemDto>,
        ultra: List<KmaItemDto>,
    ): WeatherSkySlot {
        val slot = KmaBaseTimeCalculator.forecastSlot(now, hourOffset)
        val values = ultra.filter {
            it.forecastDate == slot.date && it.forecastTime == slot.time
        }.associate { it.category.orEmpty() to it.forecastValue.orEmpty() }

        val temperature = if (hourOffset == 0) {
            ncst.observationValue("T1H")?.toFloatOrNull()
                ?: values["T1H"]?.toFloatOrNull()
        } else {
            values["T1H"]?.toFloatOrNull()
        }

        return WeatherSkySlot(
            hourOffset = hourOffset,
            temperatureCelsius = temperature,
            skyCode = values["SKY"],
            precipitationCode = values["PTY"],
            lightningValue = values["LGT"],
            forecastDate = slot.date,
            forecastTime = slot.time,
        )
    }

    private suspend fun resolveTodayDailyTemperatures(
        today: String,
        now: LocalDateTime,
        latestBase: KmaBaseTimeCalculator.BaseDateTime,
        latestItems: List<KmaItemDto>,
        nx: Int,
        ny: Int,
    ): Pair<Float?, Float?> {
        var (minValue, maxValue) = latestItems.resolveDailyMinMax(today)

        if (minValue == null || maxValue == null) {
            val supplementBase = KmaBaseTimeCalculator.vilageForecast0200Today(now)
                ?: KmaBaseTimeCalculator.vilageForecastPreviousDayLast(now)

            if (supplementBase != latestBase) {
                val supplementItems = api.getVilageForecast(
                    baseDate = supplementBase.date,
                    baseTime = supplementBase.time,
                    nx = nx,
                    ny = ny,
                ).itemsOrEmpty()
                if (minValue == null) {
                    minValue = supplementItems.dailyTemperature("TMN", today)
                }
                if (maxValue == null) {
                    maxValue = supplementItems.dailyTemperature("TMX", today)
                }
            }
        }

        return minValue?.toFloatOrNull() to maxValue?.toFloatOrNull()
    }

    private suspend fun fetchYesterdayAverageCelsius(
        now: LocalDateTime,
        nx: Int,
        ny: Int,
    ): Float? {
        val candidate = KmaBaseTimeCalculator.pastNowcastForComparison(now) ?: return null
        val response = api.getUltraShortNowcast(
            baseDate = candidate.base.date,
            baseTime = candidate.base.time,
            nx = nx,
            ny = ny,
        )
        return response.itemsOrEmpty()
            .observationValue("T1H")
            ?.toFloatOrNull()
    }

    private fun averageCelsius(min: Float?, max: Float?): Float? {
        if (min == null || max == null) return null
        return ((min + max) / 2f).roundToInt().toFloat()
    }
}
