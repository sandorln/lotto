package com.sandorln.lotto.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

object TextStyles {
    val lottoItemTiny = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 7.sp,
    )
    val lottoItemSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
    )
    val lottoItemNormal = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color.Black
    )
    val lottoItemLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.Black
    )
}