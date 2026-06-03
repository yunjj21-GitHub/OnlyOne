package com.yjp.onlyone.presentation.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

private val PetProgressRingSize = 220.dp
private val PetTextSizeReduce = 2.sp
private val HomeBackgroundGradientStops = arrayOf(
    0f to R.color.white,
    0.86f to R.color.white,
    1f to R.color.primary_blue,
)

private val HomeContentPanelCornerRadius = 14.dp
private val HomeContentPanelContentPadding = 20.dp
private val HomeContentPanelShadowElevation = 6.dp
private val HappinessIndexLabelFontSize = 19.sp
private val HappinessIndexLabelLineHeight = 26.sp
private val HomeActivityStatsTopPadding = 16.dp
private val HomeActivityStatValueTopPadding = 14.dp
private val HomeActivityStatBoneIconTopPadding = 4.dp
private val HomeActivityStatBoneIconSize = 40.dp
private val HomeActivityStatLabelFontSize = 19.sp
private val HomeActivityStatLabelLineHeight = 24.sp
private val HomeActivityStatValueFontSize = 17.sp
private val HomeActivityStatValueLineHeight = 22.sp

private data class HomeActivityStat(
    val label: String,
    val value: String,
)

private val DefaultHomeActivityStats = listOf(
    HomeActivityStat(label = "밥", value = "0회"),
    HomeActivityStat(label = "산책", value = "0M"),
    HomeActivityStat(label = "놀이", value = "0M"),
    HomeActivityStat(label = "간식", value = "0회"),
    HomeActivityStat(label = "양치", value = "X"),
)

@Composable
fun HomeScreen(
    petName: String,
    @DrawableRes petIconRes: Int = R.drawable.ic_dog1,
    happinessIndex: Int = HomeViewModel.DEFAULT_HAPPINESS_INDEX,
    daysTogether: Int = HomeViewModel.DEFAULT_DAYS_TOGETHER,
    onMemoClick: () -> Unit = {},
    onDogInfoEditClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val happinessProgress = remember(happinessIndex) {
        HomeViewModel.happinessProgress(happinessIndex)
    }
    val togetherDaysText = remember(daysTogether) {
        HomeViewModel.buildTogetherDaysText(daysTogether)
    }
    val headlineMedium = MaterialTheme.typography.headlineMedium
    val baseFontSizeValue = headlineMedium.fontSize.value
    val petNameStyle = headlineMedium.copy(
        fontSize = baseFontSizeValue.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
    )
    val togetherDaysStyle = headlineMedium.copy(
        fontSize = (baseFontSizeValue - PetTextSizeReduce.value).sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
    )

    val homeBackgroundBrush = Brush.verticalGradient(
        colorStops = HomeBackgroundGradientStops.map { (stop, colorRes) ->
            stop to colorResource(colorRes)
        }.toTypedArray(),
    )

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(homeBackgroundBrush),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeTopIconButton(
                iconRes = R.drawable.ic_speech_bubble,
                onClick = onMemoClick,
            )
            HomeTopIconButton(
                iconRes = R.drawable.ic_pencil,
                onClick = onDogInfoEditClick,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.TopCenter) {
                Box(
                    modifier = Modifier
                        .padding(top = 14.dp)
                        .size(PetProgressRingSize),
                    contentAlignment = Alignment.Center,
                ) {
                    PetProgressRing(
                        progress = happinessProgress,
                        modifier = Modifier.fillMaxSize(),
                    )
                    Image(
                        painter = painterResource(petIconRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                    )
                }
                Text(
                    text = petName,
                    modifier = Modifier.align(Alignment.TopCenter),
                    style = petNameStyle,
                )
            }
            Text(
                text = togetherDaysText,
                modifier = Modifier.padding(top = 8.dp),
                style = togetherDaysStyle,
            )
            HomeContentPanel(
                happinessIndex = happinessIndex,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
        }
    }
}

@Composable
private fun HomeContentPanel(
    happinessIndex: Int,
    modifier: Modifier = Modifier,
) {
    val happinessIndexLabelStyle = MaterialTheme.typography.titleMedium.copy(
        fontSize = HappinessIndexLabelFontSize,
        lineHeight = HappinessIndexLabelLineHeight,
        fontWeight = FontWeight.Black,
        color = colorResource(R.color.black),
    )
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(HomeContentPanelCornerRadius),
        color = colorResource(R.color.white),
        shadowElevation = HomeContentPanelShadowElevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HomeContentPanelContentPadding),
        ) {
            Text(
                text = "행복지수 $happinessIndex",
                style = happinessIndexLabelStyle,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = HomeActivityStatsTopPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                DefaultHomeActivityStats.forEach { stat ->
                    HomeActivityStatColumn(
                        label = stat.label,
                        value = stat.value,
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeActivityStatColumn(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val labelStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = HomeActivityStatLabelFontSize,
        lineHeight = HomeActivityStatLabelLineHeight,
        fontWeight = FontWeight.Normal,
        color = colorResource(R.color.black),
    )
    val valueStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = HomeActivityStatValueFontSize,
        lineHeight = HomeActivityStatValueLineHeight,
        fontWeight = FontWeight.Normal,
        color = colorResource(R.color.black),
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = labelStyle,
        )
        Text(
            text = value,
            modifier = Modifier.padding(top = HomeActivityStatValueTopPadding),
            style = valueStyle,
        )
        Image(
            painter = painterResource(R.drawable.ic_bone_muted),
            contentDescription = null,
            modifier = Modifier
                .padding(top = HomeActivityStatBoneIconTopPadding)
                .size(HomeActivityStatBoneIconSize),
        )
    }
}

@Composable
private fun HomeTopIconButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(48.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        shape = CircleShape,
        color = colorResource(R.color.light_gray),
        shadowElevation = 0.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    OnlyOneTheme {
        HomeScreen(
            petName = HomeViewModel.DEFAULT_PET_NAME,
            petIconRes = HomeViewModel.DEFAULT_PET_ICON_RES,
            happinessIndex = HomeViewModel.DEFAULT_HAPPINESS_INDEX,
            daysTogether = HomeViewModel.DEFAULT_DAYS_TOGETHER,
        )
    }
}
