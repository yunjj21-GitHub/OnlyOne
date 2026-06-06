package com.yjp.onlyone.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yjp.onlyone.data.remote.api.KmaWeatherApi
import com.yjp.onlyone.data.remote.dto.KmaItemsDto
import com.yjp.onlyone.data.remote.gson.KmaItemsDtoDeserializer
import com.yjp.onlyone.data.remote.interceptor.OnlyOneNetworkInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val KMA_BASE_URL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
    private const val CONNECT_TIMEOUT_SECONDS = 30L
    private const val READ_TIMEOUT_SECONDS = 30L
    private const val WRITE_TIMEOUT_SECONDS = 30L

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(KmaItemsDto::class.java, KmaItemsDtoDeserializer())
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(OnlyOneNetworkInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(KMA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideKmaWeatherApi(
        retrofit: Retrofit,
    ): KmaWeatherApi {
        return retrofit.create(KmaWeatherApi::class.java)
    }
}
