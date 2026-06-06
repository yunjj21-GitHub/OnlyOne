package com.yjp.onlyone.data.remote.api

import com.yjp.onlyone.data.remote.dto.KmaWeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 기상청 단기예보 조회서비스 (VilageFcstInfoService_2.0) Retrofit 정의.
 *
 * @see <a href="https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0">공공데이터포털 API</a>
 */
interface KmaWeatherApi {

    /**
     * 초단기실황조회 (getUltraSrtNcst)
     *
     * 실측 데이터. 주로 [com.yjp.onlyone.data.remote.dto.KmaItemDto.category] T1H(현재 기온),
     * PTY(강수형태)를 확인한다.
     *
     * - base_time: 매시 정각(HH00), API 제공은 정시 +10분 이후
     */
    @GET("getUltraSrtNcst")
    suspend fun getUltraShortNowcast(
        @Query(value = "serviceKey", encoded = true) serviceKey: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("dataType") dataType: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int,
    ): KmaWeatherResponseDto

    /**
     * 초단기예보조회 (getUltraSrtFcst)
     *
     * 현재 시각 기준 약 6시간 예보. SKY(맑음/흐림), PTY(강수), T1H(시간별 기온)를 확인한다.
     * fcstDate + fcstTime으로 예보 대상 시각을 구분한다.
     *
     * - base_time: 매시 30분(HH30), API 제공은 +45분 이후
     */
    @GET("getUltraSrtFcst")
    suspend fun getUltraShortForecast(
        @Query(value = "serviceKey", encoded = true) serviceKey: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("dataType") dataType: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int,
    ): KmaWeatherResponseDto

    /**
     * 단기예보조회 (getVilageFcst)
     *
     * 3일 단기예보. 금일 최저/최고 기온은 TMN, TMX category를 사용한다.
     *
     * - base_time: 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 중 하나
     */
    @GET("getVilageFcst")
    suspend fun getVilageForecast(
        @Query(value = "serviceKey", encoded = true) serviceKey: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("dataType") dataType: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int,
    ): KmaWeatherResponseDto

}
