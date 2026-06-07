package com.yjp.onlyone.domain.model

/** 홈 화면 날씨 카드에 필요한 데이터. */
data class HomeWeather(
    val currentTemperatureCelsius: Float?,
    val weatherCondition: String,
    val minTemperatureCelsius: Float?,
    val maxTemperatureCelsius: Float?,
    val todayAverageCelsius: Float?,
    val yesterdayAverageCelsius: Float?,
    val currentSky: WeatherSkySlot,
    /** 홈 UI 시간별 4칸 (0~3시간). */
    val hourlyForecasts: List<WeatherSkySlot>,
    /** 산책 추천용 6시간 예보 (0~5시간). */
    val walkForecastSlots: List<WeatherSkySlot>,
)

/** 시간대별 하늘/기온 슬롯. hourOffset 0=현재, 1~3=이후 시간. */
data class WeatherSkySlot(
    val hourOffset: Int,
    val temperatureCelsius: Float?,
    val skyCode: String?,
    val precipitationCode: String?,
    val lightningValue: String?,
    /** 초단기실황 RN1(1시간 강수량, mm). 현재 시각 슬롯에만 존재할 수 있다. */
    val precipitationMm: Float? = null,
    val forecastDate: String,
    val forecastTime: String,
)
