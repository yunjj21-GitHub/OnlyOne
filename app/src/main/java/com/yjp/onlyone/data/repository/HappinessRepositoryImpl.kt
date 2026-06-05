package com.yjp.onlyone.data.repository

import com.yjp.onlyone.data.local.dao.HappinessDao
import com.yjp.onlyone.data.local.mapper.toHappinessEntity
import com.yjp.onlyone.data.local.mapper.toHomeHappinessInput
import com.yjp.onlyone.domain.model.HomeHappinessInput
import com.yjp.onlyone.domain.repository.HappinessRepository
import com.yjp.onlyone.util.toEpochDayValue
import com.yjp.onlyone.util.todayLocalDate
import javax.inject.Inject

class HappinessRepositoryImpl @Inject constructor(
    private val happinessDao: HappinessDao,
) : HappinessRepository {

    override suspend fun getHappinessInput(): HomeHappinessInput {
        val todayEpochDay = todayLocalDate().toEpochDayValue()
        val entity = happinessDao.get() ?: return HomeHappinessInput()
        if (entity.recordDateEpochDay != todayEpochDay) {
            return HomeHappinessInput()
        }
        return entity.toHomeHappinessInput()
    }

    override suspend fun saveHappinessInput(input: HomeHappinessInput) {
        happinessDao.upsert(
            input.toHappinessEntity(todayLocalDate().toEpochDayValue()),
        )
    }
}
