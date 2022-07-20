package com.sandorln.lotto.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sandorln.lotto.util.verifyNumbers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                        when (event) {
                            is LottoPullNumberEvent.ChangeUserPickNumber -> {
                                val tempMap = _lottoPullNumberState.value.pullNumberMap.toMutableMap()
                                if (event.number != null)
                                    tempMap[event.type] = event.number
                                else
                                    tempMap.remove(event.type)
                                val copy = _lottoPullNumberState.value.copy(isLoading = false, pullNumberMap = tempMap)
                                _lottoPullNumberState.emit(copy)
                            }
                        }
                    }
            }
        }
    }
}

data class LottoPullNumberState(
    val isLoading: Boolean = false,
    val pullNumberMap: MutableMap<LottoNumberType, Int> = mutableMapOf()
) {
    val verifyNumbers: Boolean get() = pullNumberMap.verifyNumbers()
}

enum class LottoNumberType {
    ONE, TWO, THREE, FOUR, FIVE, SIX, BONUS
}

sealed interface LottoPullNumberEvent {
    data class ChangeUserPickNumber(val type: LottoNumberType, val number: Int?) : LottoPullNumberEvent
}