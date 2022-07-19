package com.sandorln.lotto.usecase

import com.sandorln.lotto.data.model.LottoPrize
import com.sandorln.lotto.data.repository.LottoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNextLottoPrizeNumberList @Inject constructor(
    private val lottoRepository: LottoRepository
) {
    suspend operator fun invoke(lottoTh: Int = 0): List<LottoPrize> =
        withContext(Dispatchers.IO) {
            lottoRepository.getNextLottoPrizeNumList(lottoTh)
        }
}