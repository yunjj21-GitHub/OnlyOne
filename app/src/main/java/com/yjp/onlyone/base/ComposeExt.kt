package com.yjp.onlyone.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.yjp.onlyone.ui.theme.OnlyOneTheme

fun ComposeView.setThemeContent(content: @Composable () -> Unit) {
    setContent {
        OnlyOneTheme(content = content)
    }
}
