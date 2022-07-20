package com.sandorln.lotto.ui.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sandorln.lotto.ui.theme.BaseComposableSize
import com.sandorln.lotto.ui.theme.DefaultSize

@Composable
fun LoadingProgressIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(DefaultSize.largeSize), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(BaseComposableSize.progressIndicatorSize))
    }
}