package com.yjp.onlyone.presentation.doginfoedit

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
private val DogInfoEditPetIconCircleSize = 210.dp
private val DogInfoEditPetIconInnerPadding = 4.dp
private val DogInfoEditIconSectionTopPadding = 32.dp
private val DogInfoEditIconSectionHorizontalPadding = 18.dp
private val DogInfoEditIconLabelToPickerTopPadding = 14.dp
private const val DogInfoEditIconPickerColumnCount = 4
private val DogInfoEditIconPickerItemInnerPadding = 2.dp
private val DogInfoEditIconPickerItemSpacing = 10.dp
private val DogInfoEditIconPickerRowSpacing = 10.dp
private val DogInfoEditIconPickerUnselectedBorderWidth = 0.4.dp
private val DogInfoEditIconPickerSelectedBorderWidth = 2.5.dp

@Composable
fun DogInfoEditScreen(
    @DrawableRes petIconRes: Int = HomeViewModel.DEFAULT_PET_ICON_RES,
    selectablePetIconRes: List<Int> = DogInfoEditViewModel.SELECTABLE_PET_ICON_RES,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onPetIconSelect: (Int) -> Unit = {},
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
        DogInfoEditIconPickerSection(
            topBarTextStyle = topBarTextStyle,
            selectablePetIconRes = selectablePetIconRes,
            selectedPetIconRes = petIconRes,
            onPetIconSelect = onPetIconSelect,
        )
    }
}

@Composable
private fun DogInfoEditIconPickerSection(
    topBarTextStyle: TextStyle,
    selectablePetIconRes: List<Int>,
    @DrawableRes selectedPetIconRes: Int,
    onPetIconSelect: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = DogInfoEditIconSectionTopPadding,
                start = DogInfoEditIconSectionHorizontalPadding,
                end = DogInfoEditIconSectionHorizontalPadding,
            ),
    ) {
        Text(
            text = stringResource(R.string.dog_info_edit_icon_label),
            modifier = Modifier.fillMaxWidth(),
            style = topBarTextStyle,
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = DogInfoEditIconLabelToPickerTopPadding),
        ) {
            val itemSize =
                (maxWidth - DogInfoEditIconPickerItemSpacing * (DogInfoEditIconPickerColumnCount - 1)) /
                    DogInfoEditIconPickerColumnCount
            Column(
                verticalArrangement = Arrangement.spacedBy(DogInfoEditIconPickerRowSpacing),
            ) {
                selectablePetIconRes.chunked(DogInfoEditIconPickerColumnCount).forEach { rowIcons ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(DogInfoEditIconPickerItemSpacing),
                    ) {
                        rowIcons.forEach { iconRes ->
                            DogInfoEditIconPickerItem(
                                iconRes = iconRes,
                                itemSize = itemSize,
                                isSelected = iconRes == selectedPetIconRes,
                                onClick = { onPetIconSelect(iconRes) },
                            )
                        }
                        repeat(DogInfoEditIconPickerColumnCount - rowIcons.size) {
                            Spacer(Modifier.size(itemSize))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DogInfoEditIconPickerItem(
    @DrawableRes iconRes: Int,
    itemSize: Dp,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val borderModifier = if (isSelected) {
        Modifier.border(
            width = DogInfoEditIconPickerSelectedBorderWidth,
            color = colorResource(R.color.primary_blue),
            shape = CircleShape,
        )
    } else {
        Modifier.border(
            width = DogInfoEditIconPickerUnselectedBorderWidth,
            color = colorResource(R.color.black),
            shape = CircleShape,
        )
    }
    Surface(
        modifier = Modifier
            .size(itemSize)
            .then(borderModifier)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        shape = CircleShape,
        color = colorResource(R.color.white),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(DogInfoEditIconPickerItemInnerPadding),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
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
