package com.yjp.onlyone.ui.component

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

@Composable
fun rememberOnlyOneToast(): (String) -> Unit {
    val context = LocalContext.current
    val parentCompositionContext = rememberCompositionContext()
    return remember(context, parentCompositionContext) {
        { message ->
            showOnlyOneToast(
                context = context,
                message = message,
                parentCompositionContext = parentCompositionContext,
            )
        }
    }
}

@Composable
fun OnlyOneToast(
    message: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(OnlyOneToastCornerRadius),
        color = colorResource(R.color.toast_background),
    ) {
        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth()
                .padding(OnlyOneToastPadding),
            color = colorResource(R.color.white),
            fontSize = OnlyOneToastTextSize,
            lineHeight = OnlyOneToastLineHeight,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun showOnlyOneToast(
    context: Context,
    message: String,
    parentCompositionContext: CompositionContext,
) {
    val composeView = ComposeView(context).apply {
        setParentCompositionContext(parentCompositionContext)
        setViewCompositionStrategy(ViewCompositionStrategy.Default)
        setContent {
            OnlyOneTheme {
                Box(
                    modifier = Modifier.padding(
                        horizontal = OnlyOneToastHorizontalMargin,
                        vertical = OnlyOneToastVerticalMargin,
                    ),
                ) {
                    OnlyOneToast(message = message)
                }
            }
        }
    }
    (context.findActivity() as? ComponentActivity)?.let { activity ->
        composeView.setViewTreeLifecycleOwner(activity)
        composeView.setViewTreeSavedStateRegistryOwner(activity)
    }
    Toast(context).apply {
        @Suppress("DEPRECATION")
        view = composeView
        duration = Toast.LENGTH_SHORT
        setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
        show()
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private val OnlyOneToastHorizontalMargin = 24.dp
private val OnlyOneToastVerticalMargin = 32.dp
private val OnlyOneToastCornerRadius = 10.dp
private val OnlyOneToastPadding = 16.dp
private val OnlyOneToastTextSize = 16.sp
private val OnlyOneToastLineHeight = 24.sp

@Preview(showBackground = true, backgroundColor = 0xFFEAEAEA)
@Composable
private fun OnlyOneToastPreview() {
    OnlyOneTheme {
        Box(
            modifier = Modifier.padding(OnlyOneToastPadding),
        ) {
            OnlyOneToast(message = "토스트 바 입니다.")
        }
    }
}
