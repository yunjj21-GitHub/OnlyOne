package com.yjp.onlyone.domain.repository

import com.yjp.onlyone.domain.model.HomeHappinessInput

interface HappinessRepository {
    suspend fun getHappinessInput(): HomeHappinessInput
    suspend fun saveHappinessInput(input: HomeHappinessInput)
}
