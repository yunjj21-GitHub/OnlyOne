package com.yjp.onlyone.ui.component

import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

private const val LOTTIE_CANVAS_WIDTH = 360f
private const val LOTTIE_CANVAS_HEIGHT = 120f
private const val LOTTIE_CONTENT_WIDTH = 304f
private const val LOTTIE_CONTENT_HEIGHT = 108f
private const val LOTTIE_CONTENT_ASPECT_RATIO = LOTTIE_CONTENT_WIDTH / LOTTIE_CONTENT_HEIGHT
private const val LOADING_OVERLAY_SCRIM_ALPHA = 0.5f

private val DefaultLoadingLottieWidth = 72.dp

@Composable
fun OOLoadingOverlay(
    modifier: Modifier = Modifier,
    lottieWidth: Dp = DefaultLoadingLottieWidth,
    scrimColor: Color = Color.Black.copy(alpha = LOADING_OVERLAY_SCRIM_ALPHA),
    @RawRes animationRes: Int = R.raw.lottie_loading_hor,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationRes))
    val loadingHeight = lottieWidth / LOTTIE_CONTENT_ASPECT_RATIO
    val lottieRenderWidth = lottieWidth * (LOTTIE_CANVAS_WIDTH / LOTTIE_CONTENT_WIDTH)
    val lottieRenderHeight = loadingHeight * (LOTTIE_CANVAS_HEIGHT / LOTTIE_CONTENT_HEIGHT)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(scrimColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(lottieWidth)
                .aspectRatio(LOTTIE_CONTENT_ASPECT_RATIO)
                .clip(RectangleShape),
            contentAlignment = Alignment.Center,
        ) {
            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .width(lottieRenderWidth)
                    .height(lottieRenderHeight)
                    .graphicsLayer(scaleX = -1f),
                iterations = LottieConstants.IterateForever,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun OOLoadingOverlayPreview() {
    OnlyOneTheme {
        OOLoadingOverlay()
    }
}
