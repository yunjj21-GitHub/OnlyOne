package com.yjp.onlyone.presentation.doginfoedit

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yjp.onlyone.R
import com.yjp.onlyone.presentation.home.HomeViewModel
import com.yjp.onlyone.ui.modifier.dogInfoPetIconShadow
import com.yjp.onlyone.ui.theme.OnlyOneTheme

private val DogInfoEditTopBarTopPadding = 10.dp
private val DogInfoEditTopBarHorizontalPadding = 10.dp
private val DogInfoEditTopBarSideMinWidth = 48.dp
private val DogInfoEditBackButtonSize = 48.dp
private val DogInfoEditSaveEndPadding = 16.dp
private val DogInfoEditPetIconAreaTopPadding = 24.dp
private val DogInfoEditPetIconCircleSize = 220.dp
private val DogInfoEditPetIconInnerPadding = 8.dp

@Composable
fun DogInfoEditScreen(
    @DrawableRes petIconRes: Int = HomeViewModel.DEFAULT_PET_ICON_RES,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val topBarTextStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.black),
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DogInfoEditTitleBar(
            topBarTextStyle = topBarTextStyle,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = DogInfoEditPetIconAreaTopPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            DogInfoEditPetIconView(petIconRes = petIconRes)
        }
    }
}

@Composable
private fun DogInfoEditTitleBar(
    topBarTextStyle: TextStyle,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = DogInfoEditTopBarTopPadding,
                start = DogInfoEditTopBarHorizontalPadding,
                end = DogInfoEditTopBarHorizontalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = DogInfoEditTopBarSideMinWidth)
                .size(DogInfoEditBackButtonSize),
            contentAlignment = Alignment.CenterStart,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_left),
                contentDescription = null,
                modifier = Modifier
                    .size(DogInfoEditBackButtonSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBackClick,
                    ),
            )
        }
        Text(
            text = stringResource(R.string.dog_info_edit_screen_title),
            modifier = Modifier.weight(1f),
            style = topBarTextStyle,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier.defaultMinSize(minWidth = DogInfoEditTopBarSideMinWidth),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Text(
                text = stringResource(R.string.save),
                modifier = Modifier
                    .padding(end = DogInfoEditSaveEndPadding)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSaveClick,
                    ),
                style = topBarTextStyle,
            )
        }
    }
}

@Composable
private fun DogInfoEditPetIconView(
    @DrawableRes petIconRes: Int,
) {
    Surface(
        modifier = Modifier
            .dogInfoPetIconShadow()
            .size(DogInfoEditPetIconCircleSize),
        shape = CircleShape,
        color = colorResource(R.color.white),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(DogInfoEditPetIconInnerPadding),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(petIconRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DogInfoEditScreenPreview() {
    OnlyOneTheme {
        DogInfoEditScreen()
    }
}
