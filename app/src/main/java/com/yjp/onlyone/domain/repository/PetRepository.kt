package com.yjp.onlyone.domain.repository

import com.yjp.onlyone.domain.model.PetInfo

interface PetRepository {
    suspend fun getPetInfo(): PetInfo
    suspend fun savePetInfo(petInfo: PetInfo)
}
