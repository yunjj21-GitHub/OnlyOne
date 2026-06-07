package com.yjp.onlyone.domain.model

/** 현재 시각 기준 하늘 상태 (getUltraSrtFcst SKY / PTY / LGT). */
data class CurrentSkyWeather(
    val label: String,
    val skyCode: String?,
    val precipitationCode: String?,
    val lightningValue: String?,
    val isNight: Boolean,
    val sunriseTime: String,
    val sunsetTime: String,
    val forecastDate: String,
    val forecastTime: String,
)
