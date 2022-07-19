package com.sandorln.lotto.di

import com.sandorln.lotto.data.repository.LottoRepository
import com.sandorln.lotto.data.repository.LottoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLottoRepository(lottoRepositoryImpl: LottoRepositoryImpl) : LottoRepository
}