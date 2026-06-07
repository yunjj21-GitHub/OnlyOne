package com.yjp.onlyone.presentation.develop

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

@Composable
fun DevelopScreen(
    viewModel: DevelopViewModel,
    modifier: Modifier = Modifier,
) {
    val debugText by viewModel.debugText.collectAsStateWithLifecycle()
    DevelopScreen(debugText = debugText, modifier = modifier)
}

@Composable
fun DevelopScreen(
    debugText: String,
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

        Text(
            text = debugText,
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

@Preview(showBackground = true)
@Composable
private fun DevelopScreenPreview() {
    OnlyOneTheme {
        DevelopScreen(debugText = "[금일 최저 / 최고]\n최저 16° / 최고 29°\n출처 API: getVilageFcst")
    }
}
