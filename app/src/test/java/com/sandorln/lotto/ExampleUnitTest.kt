package com.sandorln.lotto

import com.sandorln.lotto.data.network.LottoApiService
import com.sandorln.lotto.viewmodel.LottoNumberType
import com.sandorln.lotto.viewmodel.SelectNumberType
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

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
        val lottoTh = resultDay / 7 + if (resultDay % 7 > 0) 1 else 0
        println("날짜 차이 ${resultDay}일")
        println("최신 회차 ${lottoTh}회")
    }

    @Test
    fun 깊은복사() {
        val original = mutableMapOf<SelectNumberType, Int>().apply {
            SelectNumberType.values().forEach {
                put(it, Random(System.currentTimeMillis()).nextInt(0, 10))
            }
        }
        val copy = original.toMutableMap()
        println("original : $original")
        println("copy : $copy")

        original[SelectNumberType.ONE] = -1

        println("value change original : $original")
        println("value change copy : $copy")
    }

    @Test
    fun 랜덤값_뽑기() {
        val selectRandomLottoMap = mutableMapOf<LottoNumberType, Int>().apply {
            put(LottoNumberType.ONE, 1)
            put(LottoNumberType.THREE, 3)
            put(LottoNumberType.FIVE, 5)
            put(LottoNumberType.BONUS, 7)
        }
        val maxCount = 100

        val randomLottos = mutableListOf<Map<LottoNumberType, Int>>()
        val initRandomLottoMap = mutableMapOf<LottoNumberType, Int?>().apply {
            LottoNumberType.values().forEach { put(it, null) }
        }

        val random = Random(System.currentTimeMillis())
        for (count in 0 until maxCount) {
            val randomLottoMap = initRandomLottoMap.toMutableMap()

            while (randomLottoMap.values.any { it == null }) {
                var randomValue: Int

                while (true) {
                    randomValue = random.nextInt(1, 46)
                    if (randomLottoMap.values.none { it == randomValue })
                        break
                }

                for (entry in randomLottoMap) {
                    if (entry.value == null) {
                        randomLottoMap[entry.key] = randomValue
                        break
                    }
                }
            }

            val notNullRandomLottoMap = mutableMapOf<LottoNumberType, Int>().apply { randomLottoMap.forEach { put(key = it.key, it.value ?: 0) } }
            randomLottos.add(notNullRandomLottoMap)
            println("$count-현재 뽑아온 값 : $notNullRandomLottoMap")
        }
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}