package com.sandorln.lotto.ui.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sandorln.lotto.R
import com.sandorln.lotto.ui.theme.*
import com.sandorln.lotto.util.getNumberColor
import com.sandorln.lotto.viewmodel.LottoNumberType
import com.sandorln.lotto.viewmodel.LottoPullNumberEvent
import com.sandorln.lotto.viewmodel.LottoPullNumberState
import com.sandorln.lotto.viewmodel.LottoPullNumberViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PullNumberScreen(
    lottoPullNumberViewModel: LottoPullNumberViewModel = hiltViewModel()
) {
    var lottoPullNumberState by remember { mutableStateOf(LottoPullNumberState()) }
    LaunchedEffect(Unit) {
        lottoPullNumberViewModel
            .lottoPullNumberState
            .collectLatest {
                lottoPullNumberState = it
            }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            UserPickNumberLayout(
                pullNumberMap = lottoPullNumberState.pullNumberMap,
                onNumberChange = { type, number ->
                    lottoPullNumberViewModel.postEvent(LottoPullNumberEvent.ChangeUserPickNumber(type, number))
                }
            )
        }
    }
}

@Composable
fun UserPickNumberLayout(pullNumberMap: Map<LottoNumberType, Int?>, onNumberChange: (LottoNumberType, Int?) -> Unit) {
    Card(
        shape = RoundedCornerShape(DefaultSize.smallSize),
        backgroundColor = Blue03,
    ) {
        UserPickBallList(pullNumberMap, onNumberChange)
    }
}

@Composable
fun UserPickBallList(pullNumberMap: Map<LottoNumberType, Int?>, onNumberChange: (LottoNumberType, Int?) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        LottoNumberType.values().forEach { type ->
            UserPickBall(number = pullNumberMap[type], onNumberChange = {
                onNumberChange(type, it)
            })
        }
    }
}

@Composable
fun UserPickBall(number: Int?, onNumberChange: (Int?) -> Unit) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .size(LottoItemSize.userPickBallSize)
            .padding(DefaultSize.veryTinySize)
            .background((number ?: 0).getNumberColor(), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = number?.toString() ?: "",
            onValueChange = { userNumberString ->
                try {
                    val userNumber = userNumberString.toInt()
                    if (userNumber in 1..45)
                        onNumberChange(userNumber)
                    else
                        onNumberChange(null)
                } catch (e: Exception) {
                    onNumberChange(null)
                }
            },
            textStyle = TextStyles.lottoItemLarge.copy(textAlign = TextAlign.Center, color = Color.White),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        )

        if (number == null)
            Icon(
                modifier = Modifier.size(DefaultSize.largeSize),
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "",
                tint = Color.White
            )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewUserPickNumberBall() {
    LottoTheme {
        UserPickBall(number = null) {}
    }
}