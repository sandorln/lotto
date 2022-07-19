package com.sandorln.lotto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sandorln.lotto.data.model.LottoPrize
import com.sandorln.lotto.ui.theme.LottoTheme
import com.sandorln.lotto.util.OnBottomReached
import com.sandorln.lotto.viewmodel.LottoEvent
import com.sandorln.lotto.viewmodel.LottoState
import com.sandorln.lotto.viewmodel.LottoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val lottoViewModel: LottoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    var lottoState by remember { mutableStateOf(LottoState()) }
                    val listState = rememberLazyListState()

                    LaunchedEffect(Unit) {
                        lottoViewModel
                            .lottoState
                            .collectLatest {
                                lottoState = it
                            }
                    }

                    listState.OnBottomReached {
                        lottoViewModel.postEvent(LottoEvent.GetNextLottoPrizeList)
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
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