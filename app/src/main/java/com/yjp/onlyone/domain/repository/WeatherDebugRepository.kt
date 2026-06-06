package com.yjp.onlyone.domain.repository

interface WeatherDebugRepository {
    suspend fun fetchDebugText(
        nx: Int,
        ny: Int,
        locationNote: String = "",
    ): String
}
