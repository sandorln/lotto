package com.sandorln.lotto.ui.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sandorln.lotto.R
import com.sandorln.lotto.data.model.LottoPrize
import com.sandorln.lotto.ui.theme.*
import com.sandorln.lotto.util.OnBottomReached
import com.sandorln.lotto.util.getDecimalFormat
import com.sandorln.lotto.util.getNumberColor
import com.sandorln.lotto.viewmodel.LottoEvent
import com.sandorln.lotto.viewmodel.LottoState
import com.sandorln.lotto.viewmodel.LottoViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(lottoViewModel: LottoViewModel = hiltViewModel()) {
    var lottoState by remember { mutableStateOf(LottoState()) }
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        lottoViewModel
            .lottoState
            .collectLatest { lottoState = it }
    }

    // 무한 스크롤
    listState.OnBottomReached {
        lottoViewModel.postEvent(LottoEvent.GetNextLottoPrizeList)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(DefaultSize.normalSize),
            contentPadding = PaddingValues(horizontal = DefaultSize.largeSize, vertical = DefaultSize.normalSize)
        ) {
            items(items = lottoState.lottoPrizeList, key = { it.drwNo }) {
                LottoPrizeItem(lottoPrize = it)
            }

            if (lottoState.hasNextLottoPrizeList)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(DefaultSize.largeSize), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(BaseComposableSize.progressIndicatorSize))
                    }
                }
        }
    }
}

@Composable
fun LottoPrizeItem(lottoPrize: LottoPrize) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(DefaultSize.largeSize),
        backgroundColor = Blue03,
        elevation = DefaultSize.tinySize
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = DefaultSize.smallSize),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottoDateText(lottoPrize = lottoPrize)
            Spacer(modifier = Modifier.height(DefaultSize.smallSize))
            LottoBallList(lottoPrize = lottoPrize)
        }
    }
}

@Composable
fun LottoDateText(modifier: Modifier = Modifier, lottoPrize: LottoPrize) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .defaultMinSize(minHeight = LottoItemSize.dateMinHeight)
    ) {
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.drw_no, lottoPrize.drwNo), style = TextStyles.lottoItemNormal)
            Text(text = lottoPrize.drwNoDate, style = TextStyles.lottoItemSmall, color = Color.Gray)
        }
        Divider(
            modifier = Modifier
                .width(BaseComposableSize.dividerWidth)
                .fillMaxHeight()
                .padding(vertical = DefaultSize.smallSize),
            color = Color.Gray
        )
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.first_th), style = TextStyles.lottoItemSmall, modifier = Modifier
                        .background(Blue01, RoundedCornerShape(DefaultSize.tinySize))
                        .padding(LottoItemSize.firstTextPadding),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(DefaultSize.smallSize))
                Text(text = stringResource(id = R.string.frist_przwner_co, lottoPrize.firstPrzwnerCo), style = TextStyles.lottoItemSmall, color = Color.Gray)
            }
            Text(text = stringResource(id = R.string.first_winamnt, lottoPrize.firstWinamnt.getDecimalFormat()), style = TextStyles.lottoItemNormal)
        }
    }
}

val lottoPrize = LottoPrize(
    drwNo = 200,
    drwtNo1 = 1,
    drwtNo2 = 11,
    drwtNo3 = 21,
    drwtNo4 = 31,
    drwtNo5 = 41,
    drwtNo6 = 45,
    bnusNo = 10,
    totSellamnt = 100000,
    firstPrzwnerCo = 99,
    firstWinamnt = 200000,
    firstAccumamnt = 0,
    drwNoDate = "2022-07-13",
    returnValue = "success"
)

@Composable
@Preview(showBackground = true)
fun PreviewLottoDateText() {
    LottoTheme {
        LottoDateText(
            lottoPrize = lottoPrize
        )
    }
}

@Composable
fun LottoBall(number: Int) {
    Box(
        modifier = Modifier
            .size(BaseComposableSize.circleNumberSize)
            .padding(DefaultSize.veryTinySize)
            .background(number.getNumberColor(), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = number.toString(), style = TextStyles.lottoItemLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLottoBall() {
    LottoTheme {
        LottoBallList(lottoPrize)
    }
}

@Composable
fun LottoBallList(lottoPrize: LottoPrize) {
    val numberList = lottoPrize.getNumberList()
    Row(
        modifier = Modifier
            .background(Color.White, CircleShape)
            .padding(LottoItemSize.ballListInPadding),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        numberList.forEachIndexed { index, i ->
            if (numberList.lastIndex == index)
                Text(text = "+")
            LottoBall(number = i)
        }
    }
}