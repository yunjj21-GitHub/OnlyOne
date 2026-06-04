package com.yjp.onlyone.domain.model

import androidx.annotation.DrawableRes
import java.time.LocalDate

data class PetInfo(
    @DrawableRes val iconRes: Int,
    val name: String,
    val adoptionDate: LocalDate,
)
