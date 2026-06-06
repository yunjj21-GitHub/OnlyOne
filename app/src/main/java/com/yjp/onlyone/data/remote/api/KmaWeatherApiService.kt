package com.yjp.onlyone.data.remote.api

import com.yjp.onlyone.BuildConfig
import com.yjp.onlyone.data.remote.dto.KmaWeatherResponseDto
import com.yjp.onlyone.data.remote.dto.requireSuccess
import javax.inject.Inject
import javax.inject.Singleton

/** 기상청 날씨 API 호출 래퍼. serviceKey와 공통 파라미터를 주입한다. */
@Singleton
class KmaWeatherApiService @Inject constructor(
    private val api: KmaWeatherApi,
) {

    /**
     * 초단기실황조회 — 현재 기온(T1H).
     */
    suspend fun getUltraShortNowcast(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
        numOfRows: Int = DEFAULT_NCST_ROWS,
    ): KmaWeatherResponseDto {
        return api.getUltraShortNowcast(
            serviceKey = serviceKey,
            pageNo = DEFAULT_PAGE_NO,
            numOfRows = numOfRows,
            dataType = DATA_TYPE_JSON,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
        ).requireSuccess(ULTRA_SHORT_NOWCAST_URL)
    }

    /**
     * 초단기예보조회 — 현재 날씨(SKY/PTY) 및 시간별 예보(T1H).
     */
    suspend fun getUltraShortForecast(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
        numOfRows: Int = DEFAULT_FORECAST_ROWS,
    ): KmaWeatherResponseDto {
        return api.getUltraShortForecast(
            serviceKey = serviceKey,
            pageNo = DEFAULT_PAGE_NO,
            numOfRows = numOfRows,
            dataType = DATA_TYPE_JSON,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
        ).requireSuccess(ULTRA_SHORT_FORECAST_URL)
    }

    /**
     * 단기예보조회 — 금일 최저(TMN) / 최고(TMX).
     */
    suspend fun getVilageForecast(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
        numOfRows: Int = DEFAULT_FORECAST_ROWS,
    ): KmaWeatherResponseDto {
        return api.getVilageForecast(
            serviceKey = serviceKey,
            pageNo = DEFAULT_PAGE_NO,
            numOfRows = numOfRows,
            dataType = DATA_TYPE_JSON,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
        ).requireSuccess(VILAGE_FORECAST_URL)
    }

    private val serviceKey: String
        get() = BuildConfig.KMA_SERVICE_KEY.trim().also { key ->
            require(key.isNotBlank()) {
                "KMA_SERVICE_KEY가 비어 있습니다. local.properties에 kma.service.key를 설정하세요."
            }
        }

    private companion object {
        const val DATA_TYPE_JSON = "JSON"
        const val DEFAULT_PAGE_NO = 1
        const val DEFAULT_NCST_ROWS = 100
        const val DEFAULT_FORECAST_ROWS = 1000

        const val ULTRA_SHORT_NOWCAST_URL =
            "getUltraSrtNcst"
        const val ULTRA_SHORT_FORECAST_URL =
            "getUltraSrtFcst"
        const val VILAGE_FORECAST_URL =
            "getVilageFcst"
    }
}
