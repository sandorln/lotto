package com.sandorln.lotto.util

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
    val lottoTh = resultDay / WEEK_DAY + if (resultDay % WEEK_DAY > 0) 1 else 0

    return lottoTh.toInt()
}