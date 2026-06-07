package com.yjp.onlyone.domain.repository

import com.yjp.onlyone.domain.model.HomeWeather

interface WeatherRepository {
    suspend fun fetchHomeWeather(
        nx: Int,
        ny: Int,
        latitude: Double,
        longitude: Double,
    ): HomeWeather
}
