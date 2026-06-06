package com.yjp.onlyone.data.repository

import com.yjp.onlyone.data.remote.api.KmaWeatherApiService
import com.yjp.onlyone.data.remote.dto.dailyTemperature
import com.yjp.onlyone.data.remote.dto.itemsOrEmpty
import com.yjp.onlyone.data.remote.dto.observationValue
import com.yjp.onlyone.data.remote.dto.resolveDailyMinMax
import com.yjp.onlyone.domain.repository.WeatherDebugRepository
import com.yjp.onlyone.util.KmaBaseTimeCalculator
import com.yjp.onlyone.util.KmaWeatherCodeLabel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherDebugRepositoryImpl @Inject constructor(
    private val api: KmaWeatherApiService,
) : WeatherDebugRepository {

    override suspend fun fetchDebugText(
        nx: Int,
        ny: Int,
        locationNote: String,
    ): String = withContext(Dispatchers.IO) {
        val now = LocalDateTime.now()
        val today = KmaBaseTimeCalculator.todayDate(now)
        val ncstBase = KmaBaseTimeCalculator.nowcast(now)
        val ultraBase = KmaBaseTimeCalculator.ultraShortForecast(now)
        val vilageBase = KmaBaseTimeCalculator.vilageForecast(now)

        val ncst = api.getUltraShortNowcast(ncstBase.date, ncstBase.time, nx, ny).itemsOrEmpty()
        val ultra = api.getUltraShortForecast(ultraBase.date, ultraBase.time, nx, ny).itemsOrEmpty()
        val vilage = api.getVilageForecast(vilageBase.date, vilageBase.time, nx, ny).itemsOrEmpty()
        val pastTemp = fetchRecentNowcastTemperature(now, nx, ny)
        val dailyTemps = resolveTodayDailyTemperatures(
            today = today,
            now = now,
            latestBase = vilageBase,
            latestItems = vilage,
            nx = nx,
            ny = ny,
        )
        val todayAvg = avg(dailyTemps.minTemp, dailyTemps.maxTemp)

        buildString {
            appendLine("[조회 좌표 / base_time]")
            appendLine("nx=$nx, ny=$ny")
            if (locationNote.isNotBlank()) {
                appendLine(locationNote)
            }
            appendLine("Ncst ${ncstBase.label()}")
            appendLine("Ultra ${ultraBase.label()} / Vilage ${vilageBase.label()}")
            appendLine()

            appendLine("[금일 최저 / 최고]")
            appendLine("최저 ${dailyTemps.minTemp} / 최고 ${dailyTemps.maxTemp}")
            appendLine("출처 API: getVilageFcst")
            appendLine("필드: TMN, TMX (fcstDate=$today)")
            appendLine("조회: ${dailyTemps.sourceNote}")
            appendLine()

            appendLine("[시간별 기온 / 날씨 (현재 ~ +3시간)]")
            for (offset in 0..3) {
                appendHourlySlot(offset, now, ncst, ultra)
                appendLine()
            }

            appendLine("[금일 평균 vs 어제 평균]")
            appendLine("금일 평균: $todayAvg")
            appendLine("출처 API: getVilageFcst")
            appendLine("필드: TMN, TMX → (최저+최고)/2")
            appendLine("어제 평균: ${pastTemp.temperature}")
            appendLine("출처 API: getUltraSrtNcst")
            appendLine("필드: T1H (${pastTemp.queryLabel})")
            appendLine("비교: ${compareAvg(todayAvg, pastTemp.temperature)}")
            appendLine("참고: ${pastTemp.note}")
        }
    }

    /**
     * 금일 TMN/TMX를 조회한다.
     * 최신 단기예보 슬롯에 금일 값이 없으면 0200(또는 02:10 이전엔 전일 2300) 발표로 보완한다.
     */
    private suspend fun resolveTodayDailyTemperatures(
        today: String,
        now: LocalDateTime,
        latestBase: KmaBaseTimeCalculator.BaseDateTime,
        latestItems: List<com.yjp.onlyone.data.remote.dto.KmaItemDto>,
        nx: Int,
        ny: Int,
    ): DailyTemperatures {
        var (minValue, maxValue) = latestItems.resolveDailyMinMax(today)
        val usedBases = mutableListOf(latestBase.label())

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
                usedBases += supplementBase.label()
            }
        }

        return DailyTemperatures(
            minTemp = minValue?.temp() ?: "-",
            maxTemp = maxValue?.temp() ?: "-",
            sourceNote = usedBases.joinToString(" + "),
        )
    }

    /** 초단기실황 API 제공 범위 안에서 가장 오래된 유효 슬롯으로 과거 기온을 조회한다. */
    private suspend fun fetchRecentNowcastTemperature(
        now: LocalDateTime,
        nx: Int,
        ny: Int,
    ): PastTemperature {
        val candidate = KmaBaseTimeCalculator.pastNowcastForComparison(now)
            ?: return PastTemperature(
                temperature = "-",
                queryLabel = "조회 실패",
                note = "유효한 과거 실측 슬롯 없음",
            )

        val base = candidate.base
        val response = api.getUltraShortNowcast(
            baseDate = base.date,
            baseTime = base.time,
            nx = nx,
            ny = ny,
        )
        val temperature = response.itemsOrEmpty()
            .observationValue("T1H")
            ?.temp()
            ?: return PastTemperature(
                temperature = "-",
                queryLabel = "base_date=${base.date}, base_time=${base.time}",
                note = "유효 슬롯이지만 T1H 없음",
            )

        return PastTemperature(
            temperature = temperature,
            queryLabel = "base_date=${base.date}, base_time=${base.time}",
            note = "약 ${candidate.hoursAgo}시간 전 실측 사용 (API 제공: 최근 24시간)",
        )
    }

    private fun StringBuilder.appendHourlySlot(
        offset: Int,
        now: LocalDateTime,
        ncst: List<com.yjp.onlyone.data.remote.dto.KmaItemDto>,
        ultra: List<com.yjp.onlyone.data.remote.dto.KmaItemDto>,
    ) {
        val slot = KmaBaseTimeCalculator.forecastSlot(now, offset)
        val values = ultra.filter {
            it.forecastDate == slot.date && it.forecastTime == slot.time
        }.associate { it.category.orEmpty() to it.forecastValue.orEmpty() }

        val label = if (offset == 0) "현재" else "+${offset}시간"
        val timeLabel = formatSlotTime(slot.date, slot.time)
        val temp = if (offset == 0) {
            ncst.observationValue("T1H")?.temp() ?: "-"
        } else {
            values["T1H"]?.temp() ?: "-"
        }
        val tempApi = if (offset == 0) "getUltraSrtNcst / T1H" else "getUltraSrtFcst / T1H"
        val weather = KmaWeatherCodeLabel.weather(values["SKY"], values["PTY"], shortTerm = true)

        appendLine("$label ($timeLabel)")
        appendLine("  기온: $temp")
        appendLine("    └ API: $tempApi")
        appendLine("  날씨: $weather (SKY=${values["SKY"] ?: "-"}, PTY=${values["PTY"] ?: "-"})")
        appendLine("    └ API: getUltraSrtFcst / SKY, PTY")
    }

    private fun formatSlotTime(date: String, time: String): String {
        val localDate = java.time.LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"))
        val hour = time.take(2).toIntOrNull() ?: 0
        return "${localDate.monthValue}/${localDate.dayOfMonth} ${hour}시"
    }

    private fun String.temp(): String {
        return toFloatOrNull()?.let { "${it.roundToInt()}°" } ?: this
    }

    private fun avg(min: String, max: String): String {
        val a = min.removeSuffix("°").toFloatOrNull()
        val b = max.removeSuffix("°").toFloatOrNull()
        if (a == null || b == null) return "-"
        return "${((a + b) / 2f).roundToInt()}°"
    }

    private fun compareAvg(today: String, yesterday: String): String {
        val a = today.removeSuffix("°").toFloatOrNull()
        val b = yesterday.removeSuffix("°").toFloatOrNull()
        if (a == null || b == null) return "-"
        val diff = (a - b).roundToInt()
        return when {
            diff > 0 -> "어제 평균 대비 ${diff}° 높음"
            diff < 0 -> "어제 평균 대비 ${kotlin.math.abs(diff)}° 낮음"
            else -> "어제 평균과 동일"
        }
    }

    private data class PastTemperature(
        val temperature: String,
        val queryLabel: String,
        val note: String,
    )

    private data class DailyTemperatures(
        val minTemp: String,
        val maxTemp: String,
        val sourceNote: String,
    )

}
