package com.yjp.onlyone.presentation.develop

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

private val DevelopScreenTitleBottomPadding = 8.dp
private val DevelopScreenFlowerSize = 140.dp
private val DevelopScreenTitleLineHeight = 28.sp
private val DevelopScreenCautionLineHeight = 24.sp
private val DevelopScreenSkyIconSize = 88.dp

@Composable
fun DevelopScreen(
    viewModel: DevelopViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DevelopScreen(uiState = uiState, modifier = modifier)
}

@Composable
fun DevelopScreen(
    uiState: DevelopUiState,
    modifier: Modifier = Modifier,
) {
    val titleStyle = MaterialTheme.typography.titleLarge.copy(
        fontSize = 32.sp,
        lineHeight = DevelopScreenTitleLineHeight,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.black),
    )
    val cautionStyle = MaterialTheme.typography.titleMedium.copy(
        fontSize = 22.sp,
        lineHeight = DevelopScreenCautionLineHeight,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.caution),
    )
    val debugStyle = MaterialTheme.typography.bodySmall.copy(
        fontFamily = FontFamily.Monospace,
        color = colorResource(R.color.black),
        lineHeight = 18.sp,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .padding(28.dp),
    ) {
        Text(
            text = stringResource(R.string.develop_weather_api_title),
            style = titleStyle,
        )
        Text(
            text = stringResource(R.string.develop_caution_message),
            modifier = Modifier.padding(top = DevelopScreenTitleBottomPadding),
            style = cautionStyle,
        )

        if (uiState.skyWeatherIconRes != null) {
            DevelopSkyWeatherPreview(
                iconRes = uiState.skyWeatherIconRes,
                label = uiState.skyWeatherLabel,
                detail = uiState.skyWeatherDetail,
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        Text(
            text = uiState.debugText,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp),
            style = debugStyle,
        )

        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_flower2),
                contentDescription = null,
                modifier = Modifier
                    .size(DevelopScreenFlowerSize)
                    .offset(x = (-26).dp),
                contentScale = ContentScale.Fit,
            )
            Image(
                painter = painterResource(R.drawable.ic_flower1),
                contentDescription = null,
                modifier = Modifier
                    .offset(x = (-84).dp, y = 8.dp)
                    .size(DevelopScreenFlowerSize),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
private fun DevelopSkyWeatherPreview(
    @DrawableRes iconRes: Int,
    label: String,
    detail: String,
    modifier: Modifier = Modifier,
) {
    val black = colorResource(R.color.black)
    val gray = colorResource(R.color.sub_text)
    val sectionTitleStyle = MaterialTheme.typography.titleMedium.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = black,
    )
    val labelStyle = MaterialTheme.typography.titleLarge.copy(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = black,
    )
    val detailStyle = MaterialTheme.typography.bodySmall.copy(
        fontFamily = FontFamily.Monospace,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = gray,
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(DevelopScreenSkyIconSize),
            contentScale = ContentScale.Fit,
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.develop_current_sky_title),
                style = sectionTitleStyle,
            )
            Text(
                text = label,
                modifier = Modifier.padding(top = 4.dp),
                style = labelStyle,
            )
            Text(
                text = detail,
                modifier = Modifier.padding(top = 4.dp),
                style = detailStyle,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DevelopScreenPreview() {
    OnlyOneTheme {
        DevelopScreen(
            uiState = DevelopUiState(
                skyWeatherIconRes = R.drawable.ic_cloudy,
                skyWeatherLabel = "흐림",
                skyWeatherDetail = "SKY=4 PTY=0 LGT=0 (밤, 일출 05:12 일몰 19:48) · 20260606 2300",
                debugText = "[금일 최저 / 최고]\n최저 16° / 최고 29°",
            ),
        )
    }
}
