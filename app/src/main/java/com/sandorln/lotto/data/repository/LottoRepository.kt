package com.sandorln.lotto.data.repository

import com.sandorln.lotto.data.model.LottoPrize

interface LottoRepository {
    suspend fun getNextLottoPrizeNumList(lottoTh: Int): List<LottoPrize>
}