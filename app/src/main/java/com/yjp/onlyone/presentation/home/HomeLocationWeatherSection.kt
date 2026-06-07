package com.yjp.onlyone.presentation.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

private val HomeWeatherSectionTopPadding = 28.dp
private val HomeWeatherSectionBottomPadding = 24.dp
private val HomeWeatherTitleFontSize = 22.sp
private val HomeWeatherTitleLineHeight = 32.sp
private val HomeWeatherLocationIconSize = 22.dp
private val HomeWeatherLocationTextFontSize = 16.sp
private val HomeWeatherLocationTextLineHeight = 20.sp
private val HomeWeatherCurrentIconSize = 120.dp
private val HomeWeatherCurrentTemperatureFontSize = 28.sp
private val HomeWeatherCurrentTemperatureLineHeight = 42.sp
private val HomeWeatherDetailFontSize = 16.sp
private val HomeWeatherDetailLineHeight = 26.sp
private val HomeWeatherHighLowFontSize = 14.sp
private val HomeWeatherHighLowLineHeight = 20.sp
private val HomeWeatherHourlyTopPadding = 14.dp
private val HomeWeatherHourlyIconSize = 50.dp
private val HomeWeatherHourlyTimeFontSize = 14.sp
private val HomeWeatherHourlyTimeLineHeight = 18.sp
private val HomeWeatherHourlyTemperatureFontSize = 14.sp
private val HomeWeatherHourlyTemperatureLineHeight = 20.sp
private const val DEFAULT_HOURLY_FORECAST_COUNT = 4

@Composable
fun HomeLocationWeatherSection(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.home_weather_walk_title_default),
    locationAddress: String = "",
    currentTemperature: String = stringResource(R.string.home_weather_temperature_default),
    temperatureComparison: String = stringResource(R.string.home_weather_temperature_comparison_default),
    weatherCondition: String = stringResource(R.string.home_weather_condition_default),
    highTemperature: String = "0°",
    lowTemperature: String = "0°",
    hourlyForecasts: List<HomeWeatherHourlyUi> = defaultHourlyForecasts(),
    @DrawableRes currentWeatherIconRes: Int = R.drawable.ic_sunny,
) {
    val black = colorResource(R.color.black)
    val gray = colorResource(R.color.sub_text)
    val titleStyle = MaterialTheme.typography.titleMedium.copy(
        fontSize = HomeWeatherTitleFontSize,
        lineHeight = HomeWeatherTitleLineHeight,
        fontWeight = FontWeight.Bold,
        color = black,
    )
    val locationStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = HomeWeatherLocationTextFontSize,
        lineHeight = HomeWeatherLocationTextLineHeight,
        fontWeight = FontWeight.Normal,
        color = gray,
    )
    val temperatureStyle = MaterialTheme.typography.headlineLarge.copy(
        fontSize = HomeWeatherCurrentTemperatureFontSize,
        lineHeight = HomeWeatherCurrentTemperatureLineHeight,
        fontWeight = FontWeight.Bold,
        color = black,
    )
    val detailStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = HomeWeatherDetailFontSize,
        lineHeight = HomeWeatherDetailLineHeight,
        fontWeight = FontWeight.Normal,
        color = black,
    )
    val highLowStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = HomeWeatherHighLowFontSize,
        lineHeight = HomeWeatherHighLowLineHeight,
        fontWeight = FontWeight.Normal,
        color = black,
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 6.dp)
    ) {
        Text(
            text = title,
            style = titleStyle,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_cur_loc),
                contentDescription = null,
                modifier =
                    Modifier.size(HomeWeatherLocationIconSize).padding(end = 2.dp),
                colorFilter = ColorFilter.tint(gray),
            )
            if (locationAddress.isNotEmpty()) {
                Text(
                    text = locationAddress,
                    style = locationStyle,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(currentWeatherIconRes),
                contentDescription = null,
                modifier = Modifier.size(HomeWeatherCurrentIconSize),
                contentScale = ContentScale.Fit,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = currentTemperature,
                    style = temperatureStyle,
                )
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = temperatureComparison,
                    style = detailStyle,
                )
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = weatherCondition,
                    style = detailStyle,
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("최고 ")
                        }
                        append(highTemperature)
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("최저 ")
                        }
                        append(lowTemperature)
                    },
                    style = highLowStyle,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val displayHourlyForecasts = hourlyForecasts.ifEmpty { defaultHourlyForecasts() }
            displayHourlyForecasts.forEach { forecast ->
                HomeWeatherHourlyItem(
                    forecast = forecast,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun HomeWeatherHourlyItem(
    forecast: HomeWeatherHourlyUi,
    modifier: Modifier = Modifier,
) {
    val black = colorResource(R.color.black)
    val timeStyle = MaterialTheme.typography.bodySmall.copy(
        fontSize = HomeWeatherHourlyTimeFontSize,
        lineHeight = HomeWeatherHourlyTimeLineHeight,
        fontWeight = FontWeight.Normal,
        color = black,
    )
    val temperatureStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = HomeWeatherHourlyTemperatureFontSize,
        lineHeight = HomeWeatherHourlyTemperatureLineHeight,
        fontWeight = FontWeight.Normal,
        color = black,
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = forecast.timeLabel,
            style = timeStyle,
        )
        Image(
            painter = painterResource(forecast.iconRes),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 4.dp)
                .size(HomeWeatherHourlyIconSize),
            contentScale = ContentScale.Fit,
        )
        Text(
            text = forecast.temperature,
            modifier = Modifier.padding(top = 4.dp),
            style = temperatureStyle,
        )
    }
}

data class HomeWeatherHourlyUi(
    val timeLabel: String,
    val temperature: String,
    @DrawableRes val iconRes: Int = R.drawable.ic_sunny,
)

private fun defaultHourlyForecasts(): List<HomeWeatherHourlyUi> {
    val timeLabel = "오후 12시"
    val temperature = "0°"
    return List(DEFAULT_HOURLY_FORECAST_COUNT) {
        HomeWeatherHourlyUi(
            timeLabel = timeLabel,
            temperature = temperature,
            iconRes = R.drawable.ic_sunny,
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun HomeLocationWeatherSectionPreview() {
    OnlyOneTheme {
        HomeLocationWeatherSection(
            locationAddress = "서울특별시 중구 정동",
        )
    }
}
