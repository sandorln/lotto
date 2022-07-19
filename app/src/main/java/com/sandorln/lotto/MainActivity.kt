package com.sandorln.lotto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.sandorln.lotto.data.model.LottoPrize
import com.sandorln.lotto.data.repository.LottoRepository
import com.sandorln.lotto.ui.theme.LottoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var lottoRepository: LottoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    var result by remember { mutableStateOf<MutableList<LottoPrize>>(mutableListOf()) }

                    LaunchedEffect(Unit) {
                        withContext(Dispatchers.IO) {
                            val temp = result.toMutableList()
                            temp.addAll(lottoRepository.getNextLottoPrizeNumList(0))
                            result = temp
                        }
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = result) {
                            LottoPrizeItem(lottoPrize = it)
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