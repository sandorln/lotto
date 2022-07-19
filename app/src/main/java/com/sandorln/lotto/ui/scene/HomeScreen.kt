package com.sandorln.lotto.ui.scene

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sandorln.lotto.data.model.LottoPrize
import com.sandorln.lotto.util.OnBottomReached
import com.sandorln.lotto.viewmodel.LottoEvent
import com.sandorln.lotto.viewmodel.LottoState
import com.sandorln.lotto.viewmodel.LottoViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    lottoViewModel: LottoViewModel = hiltViewModel(),
) {
    val saveListState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()) {
        var lottoState by remember { mutableStateOf(LottoState()) }

        LaunchedEffect(Unit) {
            lottoViewModel
                .lottoState
                .collectLatest {
                    lottoState = it
                }
        }

        saveListState.OnBottomReached {
            lottoViewModel.postEvent(LottoEvent.GetNextLottoPrizeList)
        }

        LazyColumn(modifier = Modifier.fillMaxSize(), state = saveListState) {
            items(items = lottoState.lottoPrizeList) {
                LottoPrizeItem(lottoPrize = it)
            }
            if (lottoState.hasNextLottoPrizeList)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(40.dp))
                    }
                }
        }
    }
}

@Composable
fun LottoPrizeItem(lottoPrize: LottoPrize) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "제 ${lottoPrize.drwNo} 회 (${lottoPrize.drwNoDate})")
        Text(text = "당첨 번호 ${lottoPrize.drwtNo1}, ${lottoPrize.drwtNo2}, ${lottoPrize.drwtNo3}, ${lottoPrize.drwtNo4}, ${lottoPrize.drwtNo5}, ${lottoPrize.drwtNo6}")
        Text(text = "보너스 번호 ${lottoPrize.bnusNo}")
        Text(text = "1등 상금 ${lottoPrize.firstWinamnt}")
        Text(text = "1등 당첨 수 ${lottoPrize.firstPrzwnerCo}")
        Text(text = "총 상금 ${lottoPrize.totSellamnt}")
    }
}