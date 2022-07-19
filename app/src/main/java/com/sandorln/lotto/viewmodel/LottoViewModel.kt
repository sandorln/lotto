package com.sandorln.lotto.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sandorln.lotto.data.model.LottoPrize
import com.sandorln.lotto.usecase.GetNextLottoPrizeNumberList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LottoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val getNextLottoPrizeNumberList: GetNextLottoPrizeNumberList
) : AndroidViewModel(context as Application) {
    private val _initLottoState = LottoState()
    private val _lottoState: MutableStateFlow<LottoState> = MutableStateFlow(_initLottoState)
    val lottoState: StateFlow<LottoState> = _lottoState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _initLottoState)

    private val _lottoEvent = MutableSharedFlow<LottoEvent>()
    fun postEvent(lottoEvent: LottoEvent) {
        viewModelScope.launch { _lottoEvent.emit(lottoEvent) }
    }

    private lateinit var getNextLottoListJob: Job

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                _lottoEvent
                    .collect { event ->
                        when (event) {
                            LottoEvent.GetNextLottoPrizeList -> {
                                if (::getNextLottoListJob.isInitialized && getNextLottoListJob.isActive)
                                    return@collect

                                getNextLottoListJob = launch {
                                    val tempLottoState = _lottoState.value

                                    _lottoState.emit(tempLottoState.copy(isLoading = true))
                                    val lastLottoTh = tempLottoState.lottoPrizeList.lastOrNull()?.drwNo ?: 0
                                    val result = getNextLottoPrizeNumberList(lastLottoTh)
                                    val tempLottoList = tempLottoState.lottoPrizeList.toMutableList().apply { addAll(result) }
                                    _lottoState.emit(
                                        tempLottoState.copy(
                                            isLoading = false,
                                            hasNextLottoPrizeList = result.isNotEmpty(),
                                            lottoPrizeList = tempLottoList
                                        )
                                    )
                                }
                            }
                        }
                    }
            }
            launch {
                postEvent(LottoEvent.GetNextLottoPrizeList)
            }
        }
    }
}

data class LottoState(
    val isLoading: Boolean = false,
    val lottoPrizeList: List<LottoPrize> = mutableListOf(),
    val hasNextLottoPrizeList: Boolean = true
)

sealed interface LottoEvent {
    object GetNextLottoPrizeList : LottoEvent
}