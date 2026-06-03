package com.yjp.onlyone.presentation.memo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yjp.onlyone.ui.theme.OnlyOneTheme

@Composable
fun MemoScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
private fun MemoScreenPreview() {
    OnlyOneTheme {
        MemoScreen()
    }
}
