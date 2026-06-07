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
    val hourlyForecasts: List<WeatherSkySlot>,
)

/** 시간대별 하늘/기온 슬롯. hourOffset 0=현재, 1~3=이후 시간. */
data class WeatherSkySlot(
    val hourOffset: Int,
    val temperatureCelsius: Float?,
    val skyCode: String?,
    val precipitationCode: String?,
    val lightningValue: String?,
    val forecastDate: String,
    val forecastTime: String,
)
