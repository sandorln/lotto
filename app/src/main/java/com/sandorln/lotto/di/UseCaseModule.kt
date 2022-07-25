package com.sandorln.lotto.di

import com.sandorln.lotto.data.repository.LottoRepository
import com.sandorln.lotto.usecase.GetNextLottoPrizeNumberList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {
    @Provides
    fun provideGetNextLottoPrizeNumberList(lottoRepository: LottoRepository): GetNextLottoPrizeNumberList =
        GetNextLottoPrizeNumberList(lottoRepository)
}