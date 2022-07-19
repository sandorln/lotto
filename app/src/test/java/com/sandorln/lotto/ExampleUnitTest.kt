package com.sandorln.lotto

import com.sandorln.lotto.data.network.LottoApiService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    lateinit var retrofit: Retrofit
    lateinit var lottoApiService: LottoApiService

    @Before
    fun 초기화() {
        retrofit = Retrofit
            .Builder()
            .baseUrl(BuildConfig.LOTTO_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        lottoApiService = retrofit.create(LottoApiService::class.java)
    }

    @Test
    fun 로또_당첨번호_추출기() {
        runBlocking {
            val result = lottoApiService.getLottoNumber(903)
            println("로또번호 당첨 : ${result.toString()}")
        }
    }

    @Test
    fun 로또_최신회차() {
        val firstCalendar = Calendar.getInstance(Locale.KOREA).apply {
            set(2002, 11, 7, 0, 0, 0)
        }
        val secondCalendar = Calendar.getInstance(Locale.KOREA)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        println("firstCalendar 의 날짜 = ${simpleDateFormat.format(firstCalendar.time)}")
        println("secondCalendar 의 날짜 = ${simpleDateFormat.format(secondCalendar.time)}")

        val diffSec = (secondCalendar.timeInMillis - firstCalendar.timeInMillis) / 1000
        val DAY = 24 * 60 * 60
        val resultDay = diffSec / DAY
        val lottoTh = resultDay / 7 + if(resultDay % 7 > 0) 1 else 0
        println("날짜 차이 ${resultDay}일")
        println("최신 회차 ${lottoTh}회")
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}