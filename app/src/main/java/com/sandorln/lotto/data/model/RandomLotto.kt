package com.sandorln.lotto.data.model

import kotlin.random.Random

data class RandomLotto(
    val id: Int,
    var drwtNo1: Int?,
    var drwtNo2: Int?,
    var drwtNo3: Int?,
    var drwtNo4: Int?,
    var drwtNo5: Int?,
    var drwtNo6: Int?,
    var bnusNo: Int?
) {
    private fun checkValuesNotNull(): Boolean = drwtNo1 != null &&
            drwtNo2 != null &&
            drwtNo3 != null &&
            drwtNo4 != null &&
            drwtNo5 != null &&
            drwtNo6 != null &&
            bnusNo != null

    private fun checkNotOverlap(value: Int) = drwtNo1 != value &&
            drwtNo2 != value &&
            drwtNo3 != value &&
            drwtNo4 != value &&
            drwtNo5 != value &&
            drwtNo6 != value &&
            bnusNo != value

    private fun getRandomValue(): Int {
        val random = Random(System.currentTimeMillis())
        var randomValue = random.nextInt(0, 46)

        while (true) {
            if (checkNotOverlap(randomValue))
                break

            randomValue = random.nextInt(0, 46)
        }

        return randomValue
    }

    private fun inputRandomValueByNullNumber() {
        val randomValue = getRandomValue()
        when{
            drwtNo1 == null -> drwtNo1 = randomValue
        }
    }

    fun inputRandomLotto() {
        while (checkValuesNotNull()) {

        }
    }
}