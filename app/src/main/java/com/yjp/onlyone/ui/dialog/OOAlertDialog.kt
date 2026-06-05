package com.yjp.onlyone.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

const val OOAlertDialogSampleTitle = "뒤로 가시겠습니까?"
const val OOAlertDialogSampleContent =
    "저장되지 않은 변경 사항이 있습니다.\n저장하지 않고 뒤로 가시겠습니까?"

/**
 * 앱 공통 알럿 다이얼로그 (Compose 전용, 특정 화면과 연동하지 않음).
 *
 * ```
 * val alertDialog = rememberOOAlertDialog()
 * OOAlertDialogHost(
 *     state = alertDialog,
 *     onConfirm = { },
 *     onCancel = { },
 * )
 * alertDialog.show(content = "...", title = "...")
 * ```
 *
 * 미리보기: [OOAlertDialogPreview], [OOAlertDialogContentOnlyPreview]
 */
@Stable
class OOAlertDialogState {
    var request by mutableStateOf<OOAlertRequest?>(null)
        private set

    fun show(content: String, title: String? = null) {
        request = OOAlertRequest(title = title, content = content)
    }

    fun dismiss() {
        request = null
    }
}

data class OOAlertRequest(
    val title: String?,
    val content: String,
)

@Composable
fun rememberOOAlertDialog(): OOAlertDialogState = remember { OOAlertDialogState() }

@Composable
fun OOAlertDialogHost(
    state: OOAlertDialogState,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val request = state.request ?: return
    OOAlertDialog(
        title = request.title,
        content = request.content,
        onDismissRequest = state::dismiss,
        onConfirm = {
            onConfirm()
            state.dismiss()
        },
        onCancel = {
            onCancel()
            state.dismiss()
        },
    )
}

@Composable
fun OOAlertDialog(
    content: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    title: String? = null,
    confirmText: String = stringResource(R.string.confirm),
    cancelText: String = stringResource(R.string.cancel),
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        OOAlertDialogContent(
            title = title,
            content = content,
            confirmText = confirmText,
            cancelText = cancelText,
            onConfirm = onConfirm,
            onCancel = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OOAlertDialogHorizontalMargin),
        )
    }
}

@Composable
fun OOAlertDialogContent(
    content: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    confirmText: String = stringResource(R.string.confirm),
    cancelText: String = stringResource(R.string.cancel),
) {
    val black = colorResource(R.color.black)
    val white = colorResource(R.color.white)
    val slate = colorResource(R.color.slate)
    val titleStyle = ooAlertTitleTextStyle()
    val contentStyle = ooAlertContentTextStyle()
    val buttonTextStyle = ooAlertButtonTextStyle()
    val shape = RoundedCornerShape(OOAlertDialogCornerRadius)

    Surface(
        modifier = modifier.shadow(
            elevation = OOAlertDialogShadowElevation,
            shape = shape,
        ),
        shape = shape,
        color = white,
    ) {
        Column(
            modifier = Modifier.padding(OOAlertDialogPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!title.isNullOrBlank()) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    style = titleStyle,
                    color = black,
                    textAlign = TextAlign.Center,
                )
            }
            Text(
                text = content,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (title.isNullOrBlank()) 0.dp else OOAlertTitleContentSpacing),
                style = contentStyle,
                color = black,
                textAlign = TextAlign.Center,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = OOAlertContentButtonSpacing),
                horizontalArrangement = Arrangement.spacedBy(OOAlertButtonSpacing),
            ) {
                Surface(
                    onClick = onConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .height(OOAlertButtonHeight),
                    shape = RoundedCornerShape(OOAlertButtonCornerRadius),
                    color = white,
                    border = BorderStroke(OOAlertConfirmBorderWidth, slate),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = confirmText,
                            style = buttonTextStyle,
                            color = slate,
                        )
                    }
                }
                Surface(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .height(OOAlertButtonHeight),
                    shape = RoundedCornerShape(OOAlertButtonCornerRadius),
                    color = slate,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = cancelText,
                            style = buttonTextStyle,
                            color = white,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ooAlertTitleTextStyle(): TextStyle =
    MaterialTheme.typography.bodyLarge.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
    )

@Composable
private fun ooAlertContentTextStyle(): TextStyle =
    MaterialTheme.typography.bodyLarge.copy(
        fontSize = 17.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
    )

@Composable
private fun ooAlertButtonTextStyle(): TextStyle =
    MaterialTheme.typography.bodyLarge.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
    )

private val OOAlertDialogHorizontalMargin = 24.dp
private val OOAlertDialogPadding = 20.dp
private val OOAlertDialogCornerRadius = 20.dp
private val OOAlertDialogShadowElevation = 6.dp
private val OOAlertTitleContentSpacing = 12.dp
private val OOAlertContentButtonSpacing = 24.dp
private val OOAlertButtonSpacing = 12.dp
private val OOAlertButtonHeight = 48.dp
private val OOAlertButtonCornerRadius = 12.dp
private val OOAlertConfirmBorderWidth = 1.dp

@Preview(showBackground = true, backgroundColor = 0xFFEAEAEA)
@Composable
private fun OOAlertDialogPreview() {
    OnlyOneTheme {
        OOAlertDialog(
            title = OOAlertDialogSampleTitle,
            content = OOAlertDialogSampleContent,
            onDismissRequest = {},
            onConfirm = {},
            onCancel = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEAEAEA)
@Composable
private fun OOAlertDialogContentOnlyPreview() {
    OnlyOneTheme {
        OOAlertDialogContent(
            content = OOAlertDialogSampleContent,
            onConfirm = {},
            onCancel = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = OOAlertDialogHorizontalMargin),
        )
    }
}
