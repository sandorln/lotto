package com.sandorln.lotto.ui.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sandorln.lotto.R
import com.sandorln.lotto.ui.theme.*
import com.sandorln.lotto.util.getNumberColor
import com.sandorln.lotto.viewmodel.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PullNumberScreen(
    lottoPullNumberViewModel: LottoPullNumberViewModel = hiltViewModel()
) {
    var lottoPullNumberState by remember { mutableStateOf(LottoPullNumberState()) }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        lottoPullNumberViewModel
            .lottoPullNumberState
            .collectLatest {
                lottoPullNumberState = it
            }
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(DefaultSize.smallSize)) {
        item {
            UserPickNumberLayout(
                pullNumberMap = lottoPullNumberState.pullNumberMap,
                onNumberChange = { type, number -> lottoPullNumberViewModel.postEvent(LottoPullNumberEvent.ChangeUserPickNumber(type, number)) },
                selectNumberType = lottoPullNumberState.selectNumberType,
                onSelectNumberType = { lottoPullNumberViewModel.postEvent(LottoPullNumberEvent.ChangeSelectNumber(it)) },
                pullNumberEnable = lottoPullNumberState.isNotOverlapNumbers,
                onPullNumberAction = {
                    focusManager.clearFocus()
                    lottoPullNumberViewModel.postEvent(LottoPullNumberEvent.PullRandomLotto)
                }
            )
        }

        if (lottoPullNumberState.randomLottos.isNotEmpty())
            items(items = lottoPullNumberState.randomLottos) { item ->
                RandomLottoBallList(
                    pickNumberTypeList = lottoPullNumberState.pickNumberTypeList,
                    randomNumberList = item
                )
            }
    }
}

@Composable
fun UserPickNumberLayout(
    pullNumberMap: Map<LottoNumberType, Int?>,
    selectNumberType: SelectNumberType = SelectNumberType.ONE,
    onSelectNumberType: (SelectNumberType) -> Unit,
    onNumberChange: (LottoNumberType, Int?) -> Unit,
    pullNumberEnable: Boolean = false,
    onPullNumberAction: () -> Unit
) {
    Card(
        modifier = Modifier.padding(horizontal = DefaultSize.largeSize, vertical = DefaultSize.normalSize),
        shape = RoundedCornerShape(DefaultSize.largeSize),
        backgroundColor = Blue03,
        elevation = DefaultSize.tinySize
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DefaultSize.smallSize),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(id = R.string.pick_number), style = TextStyles.lottoItemNormal.copy(color = Color.Black, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(DefaultSize.tinySize))
            UserPickBallList(pullNumberMap, onNumberChange)
            Spacer(modifier = Modifier.height(DefaultSize.tinySize))
            PullNumberLayout(
                selectNumberType = selectNumberType,
                onSelectNumberType = onSelectNumberType,
                pullNumberEnable = pullNumberEnable,
                onPullNumberAction = onPullNumberAction
            )
        }
    }
}

@Composable
fun UserPickBallList(pullNumberMap: Map<LottoNumberType, Int?>, onNumberChange: (LottoNumberType, Int?) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LottoNumberType.values().forEach { type ->
            if (type == LottoNumberType.BONUS)
                Text(text = "+", textAlign = TextAlign.Center)

            UserPickBall(modifier = Modifier.weight(1f),
                number = pullNumberMap[type], onNumberChange = {
                    onNumberChange(type, it)
                })
        }
    }
}

@Composable
fun UserPickBall(modifier: Modifier = Modifier, number: Int?, onNumberChange: (Int?) -> Unit) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .aspectRatio(1f)
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
fun PullNumberLayout(
    selectNumberType: SelectNumberType = SelectNumberType.ONE,
    onSelectNumberType: (SelectNumberType) -> Unit,
    pullNumberEnable: Boolean = false,
    onPullNumberAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        SelectNumbers(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            selectNumber = selectNumberType,
            onSelectNumberType = onSelectNumberType
        )
        PullNumberButton(
            enable = pullNumberEnable,
            onClickAction = onPullNumberAction
        )
    }
}

@Composable
fun PullNumberButton(
    enable: Boolean = false,
    onClickAction: () -> Unit
) {
    TextButton(
        onClick = onClickAction,
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Blue00,
            disabledBackgroundColor = Color.Gray
        )
    ) {
        Text(text = stringResource(id = R.string.pull_number), color = Color.White)
    }
}

@Composable
fun SelectNumbers(
    modifier: Modifier = Modifier,
    selectNumber: SelectNumberType,
    onSelectNumberType: (SelectNumberType) -> Unit,
) {
    Row(modifier = modifier) {
        SelectNumberType.values().forEach { selectedType ->
            val color = if (selectNumber == selectedType) Color.White else Color.LightGray
            val bgColor = if (selectNumber == selectedType) Blue01 else Color.Unspecified
            TextButton(modifier = Modifier.weight(1f), onClick = {
                onSelectNumberType(selectedType)
            }) {
                Row(
                    modifier = Modifier
                        .background(bgColor, CircleShape)
                        .padding(horizontal = DefaultSize.tinySize),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_done),
                        tint = color,
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(DefaultSize.tinySize))
                    Text(text = selectedType.number.toString(), color = color, style = TextStyles.lottoItemSmall.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun RandomLottoBall(
    modifier: Modifier = Modifier,
    number: Int,
    isUserPickNumber: Boolean = true
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(DefaultSize.veryTinySize)
            .background(number.getNumberColor(), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = number.toString(), style = TextStyles.lottoItemLarge.copy(textAlign = TextAlign.Center, color = Color.White))

        if (isUserPickNumber)
            Icon(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(DefaultSize.normalSize),
                painter = painterResource(id = R.drawable.ic_pin),
                tint = Color.White,
                contentDescription = "",
            )
    }
}

@Composable
fun RandomLottoBallList(
    pickNumberTypeList: List<LottoNumberType>,
    randomNumberList: Map<LottoNumberType, Int>
) {
    Card(
        Modifier.padding(horizontal = DefaultSize.largeSize),
        shape = RoundedCornerShape(DefaultSize.largeSize),
        elevation = DefaultSize.tinySize,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DefaultSize.smallSize),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LottoNumberType.values().forEach {
                if (it == LottoNumberType.BONUS)
                    Text(text = "+")

                RandomLottoBall(
                    modifier = Modifier.weight(1f),
                    number = randomNumberList[it] ?: 0,
                    isUserPickNumber = pickNumberTypeList.contains(it)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewRandomLottoBall() {
    LottoTheme {
        Row(Modifier.fillMaxWidth()) {
            LottoNumberType.values().forEachIndexed { index, type ->
                RandomLottoBall(
                    modifier = Modifier.weight(1f),
                    number = type.ordinal,
                    isUserPickNumber = index % 2 == 0
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewUserPickNumberBall() {
    LottoTheme {
        PullNumberLayout(onSelectNumberType = {}, onPullNumberAction = {})
//        UserPickBall(number = null) {}
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewPullNumberButton() {
    LottoTheme {
        PullNumberButton() {}
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewUserPickNumberLayout() {
    LottoTheme {
        UserPickNumberLayout(
            pullNumberMap = mutableMapOf(),
            selectNumberType = SelectNumberType.ONE,
            onSelectNumberType = {},
            onPullNumberAction = {},
            onNumberChange = { _, _ -> })
    }
}