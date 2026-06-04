package com.yjp.onlyone.ui.modifier

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

private val SoftShadowElevation = 8.dp
private val SoftShadowAmbientColor = Color(0x28000000)
private val SoftShadowSpotColor = Color(0x45000000)

fun Modifier.softShadow(
    shape: Shape,
): Modifier = shadow(
    elevation = SoftShadowElevation,
    shape = shape,
    ambientColor = SoftShadowAmbientColor,
    spotColor = SoftShadowSpotColor,
)

private val HomeHappinessCardShadowElevation = 22.dp
private val HomeHappinessCardShadowAmbientColor = Color(0x73000000)
private val HomeHappinessCardShadowSpotColor = Color(0x8A000000)

fun Modifier.homeHappinessCardShadow(
    shape: Shape = RoundedCornerShape(14.dp),
): Modifier = shadow(
    elevation = HomeHappinessCardShadowElevation,
    shape = shape,
    ambientColor = HomeHappinessCardShadowAmbientColor,
    spotColor = HomeHappinessCardShadowSpotColor,
)

fun Modifier.dogInfoPetIconShadow(
    shape: Shape = CircleShape,
): Modifier = softShadow(shape)
