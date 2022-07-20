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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.sandorln.lotto.R
import com.sandorln.lotto.ui.theme.*
import com.sandorln.lotto.util.getNumberColor

@Composable
fun PullNumberScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            UserPickNumberLayout()
        }
    }
}

@Composable
fun UserPickNumberLayout() {
    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(DefaultSize.smallSize),
        backgroundColor = Blue03
    ) {
        UserPickBallList()
    }
}

@Composable
fun UserPickBallList() {
    val (num, onChangeNum) = remember { mutableStateOf("") }
    Row {
        UserPickBall(number = num, onNumberChange = onChangeNum)
    }
}

@Composable
fun UserPickBall(number: String, onNumberChange: (String) -> Unit) {
    val numberToInt = number.toIntOrNull() ?: -1
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .size(LottoItemSize.userPickBallSize)
            .padding(DefaultSize.veryTinySize)
            .background(numberToInt.getNumberColor(), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = number,
            onValueChange = { userNumber ->
                if (userNumber.length < 3)
                    onNumberChange(userNumber)
                else
                    onNumberChange(userNumber.substring(0, 2))
            },
            textStyle = TextStyles.lottoItemLarge.copy(textAlign = TextAlign.Center, color = Color.White),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
        )

        if (number.isEmpty())
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
        UserPickBall(number = "") {}
    }
}