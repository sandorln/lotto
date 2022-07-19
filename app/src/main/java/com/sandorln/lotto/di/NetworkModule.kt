package com.sandorln.lotto.di

import com.sandorln.lotto.BuildConfig
import com.sandorln.lotto.data.network.LottoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Named("LottoNumberRetrofit")
    @Provides
    @Singleton
    fun provideLottoNumberRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.LOTTO_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    @Provides
    @Singleton
    fun provideLottoApiService(@Named("LottoNumberRetrofit") retrofit: Retrofit): LottoApiService =
        retrofit.create(LottoApiService::class.java)
}