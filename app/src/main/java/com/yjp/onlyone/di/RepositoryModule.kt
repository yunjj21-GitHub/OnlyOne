package com.yjp.onlyone.di

import com.yjp.onlyone.data.repository.HappinessRepositoryImpl
import com.yjp.onlyone.data.repository.MemoRepositoryImpl
import com.yjp.onlyone.data.repository.PetRepositoryImpl
import com.yjp.onlyone.data.repository.WeatherDebugRepositoryImpl
import com.yjp.onlyone.data.repository.WeatherRepositoryImpl
import com.yjp.onlyone.domain.repository.HappinessRepository
import com.yjp.onlyone.domain.repository.MemoRepository
import com.yjp.onlyone.domain.repository.PetRepository
import com.yjp.onlyone.domain.repository.WeatherDebugRepository
import com.yjp.onlyone.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMemoRepository(
        impl: MemoRepositoryImpl,
    ): MemoRepository

    @Binds
    @Singleton
    abstract fun bindPetRepository(
        impl: PetRepositoryImpl,
    ): PetRepository

    @Binds
    @Singleton
    abstract fun bindHappinessRepository(
        impl: HappinessRepositoryImpl,
    ): HappinessRepository

    @Binds
    @Singleton
    abstract fun bindWeatherDebugRepository(
        impl: WeatherDebugRepositoryImpl,
    ): WeatherDebugRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl,
    ): WeatherRepository
}
