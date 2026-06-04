package com.yjp.onlyone.di

import android.content.Context
import androidx.room.Room
import com.yjp.onlyone.data.local.OnlyOneDatabase
import com.yjp.onlyone.data.local.dao.MemoDao
import com.yjp.onlyone.data.local.dao.PetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideOnlyOneDatabase(
        @ApplicationContext context: Context,
    ): OnlyOneDatabase {
        return Room.databaseBuilder(
            context,
            OnlyOneDatabase::class.java,
            "onlyone.db",
        ).build()
    }

    @Provides
    fun provideMemoDao(database: OnlyOneDatabase): MemoDao {
        return database.memoDao()
    }

    @Provides
    fun providePetDao(database: OnlyOneDatabase): PetDao {
        return database.petDao()
    }
}
