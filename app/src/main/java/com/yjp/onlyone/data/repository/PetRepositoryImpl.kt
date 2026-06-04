package com.yjp.onlyone.data.repository

import android.content.Context
import com.yjp.onlyone.R
import com.yjp.onlyone.data.local.dao.PetDao
import com.yjp.onlyone.data.local.mapper.toPetEntity
import com.yjp.onlyone.data.local.mapper.toPetInfo
import com.yjp.onlyone.domain.model.PetInfo
import com.yjp.onlyone.domain.repository.PetRepository
import com.yjp.onlyone.util.todayLocalDate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petDao: PetDao,
    @ApplicationContext private val context: Context,
) : PetRepository {

    override suspend fun getPetInfo(): PetInfo {
        return petDao.get()?.toPetInfo() ?: defaultPetInfo()
    }

    override suspend fun savePetInfo(petInfo: PetInfo) {
        petDao.upsert(petInfo.toPetEntity())
    }

    private fun defaultPetInfo(): PetInfo {
        return PetInfo(
            iconRes = R.drawable.ic_dog1,
            name = context.getString(R.string.pet_name_default),
            adoptionDate = todayLocalDate(),
        )
    }
}
