package com.yjp.onlyone.presentation.memo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

private val MemoTopBarTopPadding = 10.dp
private val MemoTopBarHorizontalPadding = 10.dp
private val MemoTopBarSideMinWidth = 48.dp
private val MemoBackButtonSize = 48.dp
private val MemoSaveEndPadding = 16.dp
private val MemoPaperCornerRadius = 28.dp
private val MemoPaperHorizontalPadding = 20.dp
private val MemoPaperTopPadding = 8.dp
private val MemoPaperBottomPadding = 16.dp
private val MemoPaperContentPadding = 16.dp

@Composable
fun MemoScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var memoContent by remember { mutableStateOf("") }
    val memoPaperColor = colorResource(R.color.memo_paper)
    val topBarTextStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.black),
    )
    val memoInputTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = colorResource(R.color.black),
    )

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = MemoTopBarTopPadding,
                    start = MemoTopBarHorizontalPadding,
                    end = MemoTopBarHorizontalPadding,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = MemoTopBarSideMinWidth)
                    .size(MemoBackButtonSize),
                contentAlignment = Alignment.CenterStart,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_left),
                    contentDescription = null,
                    modifier = Modifier
                        .size(MemoBackButtonSize)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBackClick,
                        ),
                )
            }
            Text(
                text = stringResource(R.string.memo_screen_title),
                modifier = Modifier.weight(1f),
                style = topBarTextStyle,
                textAlign = TextAlign.Center,
            )
            Box(
                modifier = Modifier.defaultMinSize(minWidth = MemoTopBarSideMinWidth),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Text(
                    text = stringResource(R.string.memo_save),
                    modifier = Modifier.padding(end = MemoSaveEndPadding),
                    style = topBarTextStyle,
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    start = MemoPaperHorizontalPadding,
                    end = MemoPaperHorizontalPadding,
                    top = MemoPaperTopPadding,
                    bottom = MemoPaperBottomPadding,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(MemoPaperCornerRadius),
                color = memoPaperColor,
            ) {
                BasicTextField(
                    value = memoContent,
                    onValueChange = { memoContent = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MemoPaperContentPadding)
                        .verticalScroll(rememberScrollState()),
                    textStyle = memoInputTextStyle,
                    cursorBrush = SolidColor(colorResource(R.color.black)),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoScreenPreview() {
    OnlyOneTheme {
        MemoScreen()
    }
}
