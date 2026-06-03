package com.yjp.onlyone.presentation.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun HomeScreen(
    petName: String,
    @DrawableRes petIconRes: Int = R.drawable.ic_dog1,
    happinessProgress: Float = HomeViewModel.DEFAULT_HAPPINESS_PROGRESS,
    daysTogether: Int = HomeViewModel.DEFAULT_DAYS_TOGETHER,
    modifier: Modifier = Modifier,
) {
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

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HomeTopIconButton(iconRes = R.drawable.ic_speech_bubble)
                HomeTopIconButton(iconRes = R.drawable.ic_pencil)
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
            }
        }
    }
}

@Composable
private fun HomeTopIconButton(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.size(48.dp),
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
            happinessProgress = HomeViewModel.DEFAULT_HAPPINESS_PROGRESS,
            daysTogether = HomeViewModel.DEFAULT_DAYS_TOGETHER,
        )
    }
}
