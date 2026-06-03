package com.yjp.onlyone.presentation.memo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

private val MemoBackButtonTopPadding = 10.dp
private val MemoBackButtonLeftPadding = 10.dp
private val MemoBackButtonSize = 48.dp

@Composable
fun MemoScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.ic_left),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = MemoBackButtonTopPadding,
                    start = MemoBackButtonLeftPadding,
                )
                .size(MemoBackButtonSize)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBackClick,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoScreenPreview() {
    OnlyOneTheme {
        MemoScreen()
    }
}
