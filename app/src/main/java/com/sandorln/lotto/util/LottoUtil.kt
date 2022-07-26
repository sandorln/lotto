package com.sandorln.lotto.util

import androidx.compose.ui.graphics.Color
import com.sandorln.lotto.ui.theme.*
import com.sandorln.lotto.viewmodel.LottoNumberType
import java.text.DecimalFormat
import java.util.*

const val FIRST_LOTTO_YEAR = 2002
const val FIRST_LOTTO_MONTH = 11    // 12월 이지만 Calender 는 11 이 12월로 인식
const val FIRST_LOTTO_DATE = 7

const val DAY = 24 * 60 * 60
const val WEEK_DAY = 7

fun latestLottoTh(): Int {
    val firstLottoCalendar = Calendar.getInstance(Locale.KOREA).apply { set(FIRST_LOTTO_YEAR, FIRST_LOTTO_MONTH, FIRST_LOTTO_DATE, 0, 0, 0) }
    val nowCalendar = Calendar.getInstance(Locale.KOREA)

    val diffSec = (nowCalendar.timeInMillis - firstLottoCalendar.timeInMillis) / 1000
    val resultDay = diffSec / DAY
    val lottoTh = resultDay / WEEK_DAY + 1

    return lottoTh.toInt()
}

fun Long.getDecimalFormat(): String {
    val decFormat = DecimalFormat("#,###")
    return decFormat.format(this)
}

fun Int.getNumberColor(): Color =
    when {
        this <= 0 -> BallErrorColor
        this <= 10 -> BallYellowColor
        this <= 20 -> BallBlueColor
        this <= 30 -> BallRedColor
        this <= 40 -> BallGrayColor
        this <= 45 -> BallGreenColor
        else -> BallErrorColor
    }

fun Map<LottoNumberType, Int>.isNotOverlapNumbers(): Boolean = try {
    forEach { entry ->
        if (count { entry.value == it.value } > 1)
            return false
    }
    true
} catch (e: Exception) {
    false
}

