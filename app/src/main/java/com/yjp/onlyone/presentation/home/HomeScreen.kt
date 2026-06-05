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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.component.SkyBlueGradientProgressRing
import com.yjp.onlyone.ui.dialog.ListPickerDialog
import com.yjp.onlyone.ui.modifier.homeHappinessCardShadow
import com.yjp.onlyone.ui.theme.OnlyOneTheme

private val HomePetProgressRingSize = 210.dp
private val PetTextSizeReduce = 2.sp
private val HomeContentPanelCornerRadius = 14.dp
private val HomeContentPanelContentPadding = 20.dp
private val HomeContentPanelShadowBleedHorizontal = 6.dp
private val HomeContentPanelShadowBleedBottom = 6.dp
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
    HomeActivityStat(label = "산책", value = "0분"),
    HomeActivityStat(label = "놀이", value = "0분"),
    HomeActivityStat(label = "간식", value = "0회"),
    HomeActivityStat(label = "양치", value = "X"),
)

/** ListPicker UI 확인용 — 산책 예시 데이터 */
private val WalkPickerPreviewTitle = "산책"
private val WalkPickerPreviewOptions = listOf("10분", "20분", "30분", "40분", "50분", "60분")
private const val WalkPickerPreviewInitialIndex = 1

@Composable
fun HomeScreen(
    petName: String,
    @DrawableRes petIconRes: Int = R.drawable.ic_dog1,
    happinessIndex: Int = HomeViewModel.DEFAULT_HAPPINESS_INDEX,
    daysTogether: Int = 0,
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
    val petNameStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
    )
    val togetherDaysStyle = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
    )

    var isWalkPickerPreviewVisible by remember { mutableStateOf(false) }

    val primaryBlue = colorResource(R.color.primary_blue)
    val homeBackgroundBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to Color.Transparent,
            0.86f to Color.Transparent,
            1f to primaryBlue,
        ),
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
                        .size(HomePetProgressRingSize),
                    contentAlignment = Alignment.Center,
                ) {
                    SkyBlueGradientProgressRing(
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
                modifier = Modifier.padding(top = 10.dp),
                style = togetherDaysStyle,
            )
            HomeContentPanel(
                happinessIndex = happinessIndex,
                onWalkStatClick = { isWalkPickerPreviewVisible = true },
                modifier = Modifier.padding(top = 16.dp),
            )
        }
        }

        if (isWalkPickerPreviewVisible) {
            ListPickerDialog(
                title = WalkPickerPreviewTitle,
                options = WalkPickerPreviewOptions,
                initialIndex = WalkPickerPreviewInitialIndex,
                onDismissRequest = { isWalkPickerPreviewVisible = false },
                onCancel = { isWalkPickerPreviewVisible = false },
                onConfirm = { isWalkPickerPreviewVisible = false },
            )
        }
    }
}

@Composable
private fun HomeContentPanel(
    happinessIndex: Int,
    onWalkStatClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val happinessIndexLabelStyle = MaterialTheme.typography.titleMedium.copy(
        fontSize = HappinessIndexLabelFontSize,
        lineHeight = HappinessIndexLabelLineHeight,
        fontWeight = FontWeight.Black,
        color = colorResource(R.color.black),
    )
    val panelShape = RoundedCornerShape(HomeContentPanelCornerRadius)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = HomeContentPanelShadowBleedHorizontal,
                end = HomeContentPanelShadowBleedHorizontal,
                bottom = HomeContentPanelShadowBleedBottom,
            ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .homeHappinessCardShadow(panelShape),
            shape = panelShape,
            color = colorResource(R.color.white),
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
                            onClick = if (stat.label == "산책") onWalkStatClick else null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeActivityStatColumn(
    label: String,
    value: String,
    onClick: (() -> Unit)? = null,
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
        modifier = modifier.then(
            if (onClick != null) {
                Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                )
            } else {
                Modifier
            },
        ),
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
            petName = "내새꾸",
            petIconRes = HomeViewModel.DEFAULT_PET_ICON_RES,
            happinessIndex = HomeViewModel.DEFAULT_HAPPINESS_INDEX,
            daysTogether = 0,
        )
    }
}
