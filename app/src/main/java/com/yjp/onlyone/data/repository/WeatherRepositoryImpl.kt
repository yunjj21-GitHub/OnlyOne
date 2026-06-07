package com.yjp.onlyone.data.repository

import com.yjp.onlyone.data.remote.api.KmaWeatherApiService
import com.yjp.onlyone.data.remote.dto.KmaItemDto
import com.yjp.onlyone.data.remote.dto.dailyTemperature
import com.yjp.onlyone.data.remote.dto.forecastValuesForSlotOrNearest
import com.yjp.onlyone.data.remote.dto.itemsOrEmpty
import com.yjp.onlyone.data.remote.dto.observationValue
import com.yjp.onlyone.data.remote.dto.resolveDailyMinMax
import com.yjp.onlyone.domain.model.HomeWeather
import com.yjp.onlyone.domain.model.WeatherSkySlot
import com.yjp.onlyone.domain.repository.WeatherRepository
import com.yjp.onlyone.util.KmaBaseTimeCalculator
import com.yjp.onlyone.util.KmaWeatherIconMapper
import com.yjp.onlyone.util.OOLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
        coroutineScope {
            val now = LocalDateTime.now()
            val today = KmaBaseTimeCalculator.todayDate(now)
            val ncstBase = KmaBaseTimeCalculator.nowcast(now)
            val ultraBase = KmaBaseTimeCalculator.ultraShortForecast(now)
            val vilageBase = KmaBaseTimeCalculator.vilageForecast(now)

            val ncstDeferred = async {
                fetchRequiredNowcastItems(ncstBase.date, ncstBase.time, nx, ny)
            }
            val ultraDeferred = async {
                fetchRequiredUltraForecastItems(ultraBase.date, ultraBase.time, nx, ny)
            }
            val vilageDeferred = async {
                fetchRequiredVilageItems(vilageBase.date, vilageBase.time, nx, ny)
            }
            val yesterdayDeferred = async {
                fetchYesterdayAverageCelsius(now, nx, ny)
            }

            val ncst = ncstDeferred.await()
            val ultra = ultraDeferred.await()
            val vilage = vilageDeferred.await()

            val (minCelsius, maxCelsius) = resolveTodayDailyTemperatures(
                today = today,
                now = now,
                latestBase = vilageBase,
                latestItems = vilage,
                nx = nx,
                ny = ny,
            )
            val todayAverage = averageCelsius(minCelsius, maxCelsius)
            val yesterdayAverage = yesterdayDeferred.await()
            val walkForecastSlots = (0..5).map { offset ->
                resolveSkySlot(
                    hourOffset = offset,
                    now = now,
                    ncst = ncst,
                    ultra = ultra,
                )
            }
            val currentSky = walkForecastSlots.first()
            val hourlyForecasts = walkForecastSlots.take(4)

            HomeWeather(
                currentTemperatureCelsius = ncst.observationValue("T1H")?.toFloatOrNull()
                    ?: currentSky.temperatureCelsius,
                weatherCondition = weatherLabel(currentSky),
                minTemperatureCelsius = minCelsius,
                maxTemperatureCelsius = maxCelsius,
                todayAverageCelsius = todayAverage,
                yesterdayAverageCelsius = yesterdayAverage,
                currentSky = currentSky,
                hourlyForecasts = hourlyForecasts,
                walkForecastSlots = walkForecastSlots,
            )
        }
    }

    private fun resolveSkySlot(
        hourOffset: Int,
        now: LocalDateTime,
        ncst: List<KmaItemDto>,
        ultra: List<KmaItemDto>,
    ): WeatherSkySlot {
        val slot = KmaBaseTimeCalculator.forecastSlot(now, hourOffset)
        val values = ultra.forecastValuesForSlotOrNearest(
            forecastDate = slot.date,
            forecastTime = slot.time,
        )

        val temperature = if (hourOffset == 0) {
            ncst.observationValue("T1H")?.toFloatOrNull()
                ?: values["T1H"]?.toFloatOrNull()
        } else {
            values["T1H"]?.toFloatOrNull()
        }

        val precipitationMm = if (hourOffset == 0) {
            ncst.observationValue("RN1")?.toFloatOrNull()
        } else {
            null
        }

        val precipitationCode = if (hourOffset == 0) {
            ncst.observationValue("PTY")?.takeIf { it.isNotBlank() } ?: values["PTY"]
        } else {
            values["PTY"]
        }

        return WeatherSkySlot(
            hourOffset = hourOffset,
            temperatureCelsius = temperature,
            skyCode = values["SKY"],
            precipitationCode = precipitationCode,
            lightningValue = values["LGT"],
            precipitationMm = precipitationMm,
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
                val supplementItems = fetchVilageItemsOrEmpty(
                    baseDate = supplementBase.date,
                    baseTime = supplementBase.time,
                    nx = nx,
                    ny = ny,
                    reason = "TMN/TMX 보완",
                )
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
        for (candidate in KmaBaseTimeCalculator.pastNowcastCandidates(now)) {
            val temperature = fetchNowcastItemsOrEmpty(
                baseDate = candidate.base.date,
                baseTime = candidate.base.time,
                nx = nx,
                ny = ny,
                reason = "어제 비교(${candidate.hoursAgo}시간 전)",
            ).observationValue("T1H")?.toFloatOrNull()

            if (temperature != null) return temperature
        }
        return null
    }

    /** 필수 조회 — 실패 시 예외를 그대로 전달한다. */
    private suspend fun fetchRequiredNowcastItems(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
    ): List<KmaItemDto> {
        return api.getUltraShortNowcast(baseDate, baseTime, nx, ny).itemsOrEmpty()
    }

    private suspend fun fetchRequiredUltraForecastItems(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
    ): List<KmaItemDto> {
        return api.getUltraShortForecast(baseDate, baseTime, nx, ny).itemsOrEmpty()
    }

    private suspend fun fetchRequiredVilageItems(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
    ): List<KmaItemDto> {
        return api.getVilageForecast(baseDate, baseTime, nx, ny).itemsOrEmpty()
    }

    /** 보조 조회 — resultCode=10 등 실패해도 홈 날씨 전체를 막지 않는다. */
    private suspend fun fetchNowcastItemsOrEmpty(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
        reason: String,
    ): List<KmaItemDto> {
        return runCatching {
            api.getUltraShortNowcast(baseDate, baseTime, nx, ny).itemsOrEmpty()
        }.onFailure { error ->
            OOLog.w("초단기실황 보조 조회 실패 ($reason, $baseDate $baseTime): ${error.message}")
        }.getOrDefault(emptyList())
    }

    private suspend fun fetchVilageItemsOrEmpty(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
        reason: String,
    ): List<KmaItemDto> {
        return runCatching {
            api.getVilageForecast(baseDate, baseTime, nx, ny).itemsOrEmpty()
        }.onFailure { error ->
            OOLog.w("단기예보 보조 조회 실패 ($reason, $baseDate $baseTime): ${error.message}")
        }.getOrDefault(emptyList())
    }

    private fun averageCelsius(min: Float?, max: Float?): Float? {
        if (min == null || max == null) return null
        return ((min + max) / 2f).roundToInt().toFloat()
    }

    private fun weatherLabel(slot: WeatherSkySlot): String {
        return KmaWeatherIconMapper.weatherLabel(
            skyCode = slot.skyCode,
            precipitationCode = slot.precipitationCode,
            lightningValue = slot.lightningValue,
        )
    }
}
