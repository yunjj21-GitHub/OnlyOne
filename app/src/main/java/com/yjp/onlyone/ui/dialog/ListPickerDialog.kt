package com.yjp.onlyone.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme
import kotlin.math.abs

/**
 * 앱 공통 리스트 Picker 다이얼로그 (Compose 전용, 특정 화면과 연동하지 않음).
 *
 * ```
 * ListPickerDialog(
 *     title = "산책",
 *     options = listOf("10M", "20M", "30M"),
 *     initialIndex = 1,
 *     onDismissRequest = { },
 *     onCancel = { },
 *     onConfirm = { index -> },
 * )
 * ```
 *
 * 미리보기: [ListPickerDialogPreview]
 */
@Composable
fun ListPickerDialog(
    title: String,
    options: List<String>,
    initialIndex: Int,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: (selectedIndex: Int) -> Unit,
) {
    if (options.isEmpty()) return

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        ListPickerDialogContent(
            title = title,
            options = options,
            initialIndex = initialIndex,
            onCancel = onCancel,
            onConfirm = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ListPickerDialogHorizontalMargin),
        )
    }
}

@Composable
private fun ListPickerDialogContent(
    title: String,
    options: List<String>,
    initialIndex: Int,
    onCancel: () -> Unit,
    onConfirm: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val safeInitialIndex = initialIndex.coerceIn(0, options.lastIndex)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = safeInitialIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val centeredIndex by remember(options) {
        derivedStateOf { listState.centeredItemIndex() }
    }

    LaunchedEffect(options, safeInitialIndex) {
        listState.scrollToItem(safeInitialIndex)
    }

    val selectedStyle = listPickerSelectedTextStyle()
    val unselectedStyle = MaterialTheme.typography.bodyLarge
    val black = colorResource(R.color.black)
    val white = colorResource(R.color.white)
    val slate = colorResource(R.color.slate)
    val muted = colorResource(R.color.bone_muted)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(ListPickerDialogCornerRadius),
        color = white,
    ) {
        Column(
            modifier = Modifier.padding(ListPickerDialogPadding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = selectedStyle,
                    color = black,
                )
                IconButton(
                    onClick = onCancel,
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = stringResource(R.string.cancel),
                        tint = black,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(ListPickerViewportHeight),
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    flingBehavior = snapFlingBehavior,
                    contentPadding = PaddingValues(vertical = ListPickerItemHeight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    itemsIndexed(options) { index, label ->
                        ListPickerOptionItem(
                            label = label,
                            isCentered = index == centeredIndex,
                            selectedStyle = selectedStyle,
                            unselectedStyle = unselectedStyle,
                            mutedColor = muted,
                            blackColor = black,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(ListPickerFadeHeight)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(white, Color.Transparent),
                            ),
                        ),
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(ListPickerFadeHeight)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, white),
                            ),
                        ),
                )
            }

            Surface(
                onClick = { onConfirm(centeredIndex.coerceIn(0, options.lastIndex)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(ListPickerConfirmHeight),
                shape = RoundedCornerShape(ListPickerConfirmCornerRadius),
                color = slate,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = selectedStyle,
                        color = white,
                    )
                }
            }
        }
    }
}

@Composable
private fun listPickerSelectedTextStyle(): TextStyle =
    MaterialTheme.typography.bodyLarge.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
    )

@Composable
private fun ListPickerOptionItem(
    label: String,
    isCentered: Boolean,
    selectedStyle: TextStyle,
    unselectedStyle: TextStyle,
    mutedColor: Color,
    blackColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ListPickerItemHeight),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = if (isCentered) selectedStyle else unselectedStyle,
            color = if (isCentered) blackColor else mutedColor,
        )
    }
}

private fun androidx.compose.foundation.lazy.LazyListState.centeredItemIndex(): Int {
    val layoutInfo = layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo
    if (visibleItems.isEmpty()) {
        return firstVisibleItemIndex.coerceAtLeast(0)
    }
    val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
    return visibleItems.minByOrNull { item ->
        abs((item.offset + item.size / 2) - viewportCenter)
    }?.index ?: firstVisibleItemIndex
}

private val ListPickerDialogHorizontalMargin = 24.dp
private val ListPickerDialogPadding = 20.dp
private val ListPickerDialogCornerRadius = 20.dp
private val ListPickerConfirmCornerRadius = 12.dp
private val ListPickerItemHeight = 40.dp
private val ListPickerViewportHeight = ListPickerItemHeight * 3
/** 상·하단 페이드. 짧을수록 인접 항목이 더 잘 보임 */
private val ListPickerFadeHeight = 14.dp
private val ListPickerConfirmHeight = 48.dp

@Preview(showBackground = true, backgroundColor = 0xFFEAEAEA)
@Composable
private fun ListPickerDialogPreview() {
    OnlyOneTheme {
        ListPickerDialog(
            title = "산책",
            options = listOf("10분", "20분", "30분", "40분", "50분", "60분"),
            initialIndex = 1,
            onDismissRequest = {},
            onCancel = {},
            onConfirm = {},
        )
    }
}
