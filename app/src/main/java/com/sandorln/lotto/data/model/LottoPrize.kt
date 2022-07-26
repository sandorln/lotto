package com.sandorln.lotto.data.model

import androidx.compose.ui.graphics.Color

data class LottoPrize(
    // 로또 번호
    val drwNo: Int,
    val drwtNo1: Int,
    val drwtNo2: Int,
    val drwtNo3: Int,
    val drwtNo4: Int,
    val drwtNo5: Int,
    val drwtNo6: Int,
    val bnusNo: Int,

    val totSellamnt: Long,      // 총 상금
    val firstPrzwnerCo: Int,    // 1등 당첨 인원
    val firstWinamnt: Long,     // 1등 상금 액

    val drwNoDate: String,      // 날짜
    val returnValue: String,    // success or fail
    val firstAccumamnt: Long,
) {
    val isSuccess : Boolean get() = returnValue == "success"
    fun getNumberList(): List<Int> = mutableListOf(drwtNo1, drwtNo2, drwtNo3, drwtNo4, drwtNo5, drwtNo6, bnusNo)
}