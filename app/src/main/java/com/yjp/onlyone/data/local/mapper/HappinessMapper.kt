package com.yjp.onlyone.data.local.mapper

import com.yjp.onlyone.data.local.entity.HappinessEntity
import com.yjp.onlyone.domain.model.HomeHappinessInput

fun HappinessEntity.toHomeHappinessInput(): HomeHappinessInput {
    return HomeHappinessInput(
        mealCount = mealCount,
        walkTotalMinutes = walkTotalMinutes,
        playTotalMinutes = playTotalMinutes,
        snackCount = snackCount,
        teethBrushed = teethBrushed,
    )
}

fun HomeHappinessInput.toHappinessEntity(recordDateEpochDay: Long): HappinessEntity {
    return HappinessEntity(
        recordDateEpochDay = recordDateEpochDay,
        mealCount = mealCount,
        walkTotalMinutes = walkTotalMinutes,
        playTotalMinutes = playTotalMinutes,
        snackCount = snackCount,
        teethBrushed = teethBrushed,
    )
}
