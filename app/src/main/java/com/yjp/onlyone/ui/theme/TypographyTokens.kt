package com.yjp.onlyone.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object TypographyTokens {
    val DisplayLarge = textStyle(57.sp, 64.sp, letterSpacing = (-0.25).sp)
    val DisplayMedium = textStyle(45.sp, 52.sp)
    val DisplaySmall = textStyle(36.sp, 44.sp)

    val HeadlineLarge = textStyle(30.sp, 40.sp)
    val HeadlineMedium = textStyle(24.sp, 36.sp)
    val HeadlineSmall = textStyle(24.sp, 32.sp)

    val TitleLarge = textStyle(20.sp, 26.sp)
    val TitleMedium = textStyle(16.sp, 24.sp, FontWeight.Medium, 0.15.sp)
    val TitleSmall = textStyle(14.sp, 20.sp, FontWeight.Medium, 0.1.sp)

    val BodyLarge = textStyle(16.sp, 24.sp, letterSpacing = 0.5.sp)
    val BodyMedium = textStyle(14.sp, 20.sp, letterSpacing = 0.25.sp)
    val BodySmall = textStyle(12.sp, 16.sp, letterSpacing = 0.4.sp)

    val LabelLarge = textStyle(14.sp, 20.sp, FontWeight.Medium, 0.1.sp)
    val LabelMedium = textStyle(12.sp, 16.sp, FontWeight.Medium, 0.5.sp)
    val LabelSmall = textStyle(11.sp, 16.sp, FontWeight.Medium, 0.5.sp)

    fun buildTypography(fontFamily: FontFamily): Typography = Typography(
        displayLarge = DisplayLarge.withFont(fontFamily),
        displayMedium = DisplayMedium.withFont(fontFamily),
        displaySmall = DisplaySmall.withFont(fontFamily),
        headlineLarge = HeadlineLarge.withFont(fontFamily),
        headlineMedium = HeadlineMedium.withFont(fontFamily),
        headlineSmall = HeadlineSmall.withFont(fontFamily),
        titleLarge = TitleLarge.withFont(fontFamily),
        titleMedium = TitleMedium.withFont(fontFamily),
        titleSmall = TitleSmall.withFont(fontFamily),
        bodyLarge = BodyLarge.withFont(fontFamily),
        bodyMedium = BodyMedium.withFont(fontFamily),
        bodySmall = BodySmall.withFont(fontFamily),
        labelLarge = LabelLarge.withFont(fontFamily),
        labelMedium = LabelMedium.withFont(fontFamily),
        labelSmall = LabelSmall.withFont(fontFamily),
    )

    private fun textStyle(
        fontSize: TextUnit,
        lineHeight: TextUnit,
        fontWeight: FontWeight = FontWeight.Normal,
        letterSpacing: TextUnit = 0.sp,
    ): TextStyle = TextStyle(
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
    )

    private fun TextStyle.withFont(fontFamily: FontFamily): TextStyle =
        copy(fontFamily = fontFamily)
}
