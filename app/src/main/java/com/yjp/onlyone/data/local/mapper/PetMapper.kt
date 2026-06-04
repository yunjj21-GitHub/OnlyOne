package com.yjp.onlyone.data.local.mapper

import com.yjp.onlyone.data.local.entity.PetEntity
import com.yjp.onlyone.domain.model.PetInfo
import com.yjp.onlyone.util.toEpochDayValue
import com.yjp.onlyone.util.toLocalDateFromEpochDay

fun PetEntity.toPetInfo(): PetInfo {
    return PetInfo(
        iconRes = iconRes,
        name = name,
        adoptionDate = adoptionDateEpochDay.toLocalDateFromEpochDay(),
    )
}

fun PetInfo.toPetEntity(): PetEntity {
    return PetEntity(
        iconRes = iconRes,
        name = name,
        adoptionDateEpochDay = adoptionDate.toEpochDayValue(),
    )
}
