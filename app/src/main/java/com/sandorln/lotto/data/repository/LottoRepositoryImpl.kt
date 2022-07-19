package com.sandorln.lotto.data.repository

import com.sandorln.lotto.data.model.LottoPrize
import com.sandorln.lotto.data.network.LottoApiService
import com.sandorln.lotto.util.latestLottoTh
import javax.inject.Inject

class LottoRepositoryImpl @Inject constructor(
    private val lottoApiService: LottoApiService
) : LottoRepository {
    companion object {
        const val LOTTO_PAGE = 15
    }

    override suspend fun getNextLottoPrizeNumList(lottoTh: Int): List<LottoPrize> {
        if (lottoTh == 1)
            return mutableListOf()

        val resultList = mutableListOf<LottoPrize>()
        val firstLottoTh = if (lottoTh == 0) latestLottoTh() else lottoTh - 1
        val lastLottoTh = (firstLottoTh - LOTTO_PAGE).coerceAtLeast(1)
        for (i in firstLottoTh downTo lastLottoTh) {
            val result = lottoApiService.getLottoNumber(i)
            resultList.add(result)
        }

        return resultList
    }
}