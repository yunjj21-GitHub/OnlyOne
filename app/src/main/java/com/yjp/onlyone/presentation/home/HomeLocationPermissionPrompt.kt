package com.yjp.onlyone.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme

private val HomeLocationPromptTopPadding = 28.dp
private val HomeLocationPromptBottomPadding = 16.dp
private val HomeLocationPromptImageWidth = 200.dp
private const val HomeLocationPromptImageAspectRatio = 466f / 535f
private val HomeLocationPromptTextOffsetY = (-21).dp
private val HomeLocationPromptContentMinHeight = 240.dp
private val HomeLocationPromptTitleFontSize = 20.sp
private val HomeLocationPromptTitleLineHeight = 24.sp
private val HomeLocationPromptDescriptionFontSize = 14.sp
private val HomeLocationPromptDescriptionLineHeight = 20.sp
private val HomeLocationPromptButtonHeight = 52.dp
private val HomeLocationPromptButtonCornerRadius = 10.dp
private val HomeLocationPromptButtonIconSize = 28.dp
private val HomeLocationPromptButtonTextFontSize = 16.sp
private val HomeLocationPromptButtonTextLineHeight = 22.sp

@Composable
fun HomeLocationPermissionPrompt(
    onAllowLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val black = colorResource(R.color.black)
    val white = colorResource(R.color.white)
    val slate = colorResource(R.color.slate)
    val titleStyle = MaterialTheme.typography.titleMedium.copy(
        fontSize = HomeLocationPromptTitleFontSize,
        lineHeight = HomeLocationPromptTitleLineHeight,
        fontWeight = FontWeight.Bold,
        color = black,
    )
    val descriptionStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = HomeLocationPromptDescriptionFontSize,
        lineHeight = HomeLocationPromptDescriptionLineHeight,
        fontWeight = FontWeight.Normal,
        color = black,
    )
    val buttonTextStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = HomeLocationPromptButtonTextFontSize,
        lineHeight = HomeLocationPromptButtonTextLineHeight,
        fontWeight = FontWeight.Bold,
        color = white,
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.img_walking_dog3),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .width(HomeLocationPromptImageWidth)
                    .aspectRatio(HomeLocationPromptImageAspectRatio),
                contentScale = ContentScale.Fit,
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(y = HomeLocationPromptTextOffsetY)
            ) {
                Text(
                    text = stringResource(R.string.home_location_prompt_title),
                    style = titleStyle,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = stringResource(R.string.home_location_prompt_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    style = descriptionStyle,
                    textAlign = TextAlign.End,
                )
            }
        }

        Surface(
            onClick = onAllowLocationClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(HomeLocationPromptButtonHeight),
            shape = RoundedCornerShape(HomeLocationPromptButtonCornerRadius),
            color = slate,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_loc),
                    contentDescription = null,
                    modifier = Modifier.size(HomeLocationPromptButtonIconSize),
                    colorFilter = ColorFilter.tint(white),
                )
                Text(
                    text = stringResource(R.string.home_location_permission_allow),
                    style = buttonTextStyle,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun HomeLocationPermissionPromptPreview() {
    OnlyOneTheme {
        HomeLocationPermissionPrompt(onAllowLocationClick = {})
    }
}
