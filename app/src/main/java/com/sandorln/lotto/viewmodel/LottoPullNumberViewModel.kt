package com.sandorln.lotto.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sandorln.lotto.util.isNotOverlapNumbers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LottoPullNumberViewModel @Inject constructor(
    @ApplicationContext context: Context
) : AndroidViewModel(context as Application) {
    private val _initLottoPullNumberState = LottoPullNumberState()
    private val _lottoPullNumberState = MutableStateFlow(_initLottoPullNumberState)
    val lottoPullNumberState = _lottoPullNumberState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _initLottoPullNumberState)

    private val _lottoPullNumberEvent = MutableSharedFlow<LottoPullNumberEvent>()
    fun postEvent(event: LottoPullNumberEvent) {
        viewModelScope.launch(Dispatchers.IO) { _lottoPullNumberEvent.emit(event) }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch(Dispatchers.IO) {
                _lottoPullNumberEvent
                    .collect { event ->
                        val copy = when (event) {
                            is LottoPullNumberEvent.ChangeUserPickNumber -> {
                                val tempMap = _lottoPullNumberState.value.pullNumberMap.toMutableMap()
                                if (event.number != null)
                                    tempMap[event.type] = event.number
                                else
                                    tempMap.remove(event.type)
                                _lottoPullNumberState.value.copy(pullNumberMap = tempMap)
                            }
                            is LottoPullNumberEvent.ChangeSelectNumber -> {
                                if (event.type == _lottoPullNumberState.value.selectNumberType)
                                    return@collect
                                _lottoPullNumberState.value.copy(selectNumberType = event.type)
                            }

                            LottoPullNumberEvent.PullRandomLotto -> {
                                val pickNumberTypeList = _lottoPullNumberState.value.pullNumberMap.keys.toList()
                                _lottoPullNumberState.value.copy(
                                    randomLottos = getRandomNumbers(),
                                    pickNumberTypeList = pickNumberTypeList
                                )
                            }
                        }
                        _lottoPullNumberState.emit(copy)
                    }
            }
        }
    }

    private fun getRandomNumbers(): List<Map<LottoNumberType, Int>> {
        val random = Random(System.currentTimeMillis())
        val randomLottos = mutableListOf<Map<LottoNumberType, Int>>()

        val initRandomLottoMap = mutableMapOf<LottoNumberType, Int?>().apply {
            LottoNumberType.values().forEach { put(it, null) }
            putAll(_lottoPullNumberState.value.pullNumberMap.toMap())
        }

        val maxCount = _lottoPullNumberState.value.selectNumberType.number

        for (count in 0 until maxCount) {
            val randomLottoMap = initRandomLottoMap.toMutableMap()

            while (randomLottoMap.values.any { it == null }) {
                var randomValue: Int

                while (true) {
                    randomValue = random.nextInt(1, 46)
                    if (randomLottoMap.none { it == random })
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
        }

        return randomLottos
    }
}

data class LottoPullNumberState(
    val isLoading: Boolean = false,

    val pullNumberMap: MutableMap<LottoNumberType, Int> = mutableMapOf(),
    val selectNumberType: SelectNumberType = SelectNumberType.ONE,

    val randomLottos: List<Map<LottoNumberType, Int>> = mutableListOf(),
    val pickNumberTypeList: List<LottoNumberType> = mutableListOf()
) {
    val isNotOverlapNumbers: Boolean get() = pullNumberMap.isNotOverlapNumbers()
}

enum class LottoNumberType {
    ONE, TWO, THREE, FOUR, FIVE, SIX, BONUS
}

enum class SelectNumberType(val number: Int) {
    ONE(1), HUNDRED(100), THOUSAND(1000)
}

sealed interface LottoPullNumberEvent {
    data class ChangeUserPickNumber(val type: LottoNumberType, val number: Int?) : LottoPullNumberEvent
    data class ChangeSelectNumber(val type: SelectNumberType) : LottoPullNumberEvent
    object PullRandomLotto : LottoPullNumberEvent
}