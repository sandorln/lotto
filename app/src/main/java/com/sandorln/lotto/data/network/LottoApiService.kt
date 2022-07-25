package com.sandorln.lotto.data.network

import com.sandorln.lotto.data.model.LottoPrize
import retrofit2.http.GET
import retrofit2.http.Query

interface LottoApiService {
    @GET("common.do?method=getLottoNumber")
    suspend fun getLottoNumber(@Query("drwNo") lottoTh: Int): LottoPrize
}