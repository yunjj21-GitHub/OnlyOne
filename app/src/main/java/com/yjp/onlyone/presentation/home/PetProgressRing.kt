package com.yjp.onlyone.presentation.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yjp.onlyone.ui.theme.PrimaryBlue

@Composable
fun PetProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 14.dp,
    primaryColor: Color = PrimaryBlue,
) {
    val sweepProgress = progress.coerceIn(0f, 1f)
    if (sweepProgress <= 0f) return

    Canvas(modifier = modifier.fillMaxSize()) {
        val strokePx = strokeWidth.toPx()
        val diameter = size.minDimension - strokePx
        val topLeft = Offset(
            (size.width - diameter) / 2f,
            (size.height - diameter) / 2f,
        )
        val arcSize = Size(diameter, diameter)
        val center = Offset(size.width / 2f, size.height / 2f)
        val sweepAngle = 360f * sweepProgress
        val fadeStart = (sweepProgress * 0.7f).coerceAtMost(sweepProgress - 0.03f)

        rotate(degrees = -90f, pivot = center) {
            drawArc(
                brush = Brush.sweepGradient(
                    0f to primaryColor,
                    fadeStart to primaryColor,
                    sweepProgress to Color.White.copy(alpha = 0f),
                    1f to Color.White.copy(alpha = 0f),
                    center = center,
                ),
                startAngle = 0f,
                sweepAngle = sweepAngle,
                topLeft = topLeft,
                size = arcSize,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
            )
        }

        val arcRadius = diameter / 2f
        drawCircle(
            color = primaryColor,
            radius = strokePx / 2f,
            center = Offset(
                x = center.x,
                y = center.y - arcRadius,
            ),
        )
    }
}
