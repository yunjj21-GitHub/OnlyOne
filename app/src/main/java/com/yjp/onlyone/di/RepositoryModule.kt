package com.yjp.onlyone.di

import com.yjp.onlyone.data.repository.MemoRepositoryImpl
import com.yjp.onlyone.domain.repository.MemoRepository
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
}
