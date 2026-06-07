package com.yjp.onlyone.domain.repository

import com.yjp.onlyone.domain.model.WeatherDebugResult

interface WeatherDebugRepository {
    suspend fun fetchDebugResult(
        nx: Int,
        ny: Int,
        latitude: Double,
        longitude: Double,
        locationNote: String = "",
    ): WeatherDebugResult
}
