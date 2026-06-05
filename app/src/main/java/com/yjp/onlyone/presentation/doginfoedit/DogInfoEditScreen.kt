package com.yjp.onlyone.presentation.doginfoedit

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
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
import androidx.compose.ui.unit.sp
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.modifier.dogInfoPetIconShadow
import com.yjp.onlyone.ui.theme.OnlyOneTheme
import com.yjp.onlyone.ui.dialog.PastOrTodayDatePickerDialog
import com.yjp.onlyone.util.formatDotSeparated
import com.yjp.onlyone.util.todayLocalDate
import java.time.LocalDate

private val DogInfoEditTopBarTopPadding = 10.dp
private val DogInfoEditTopBarHorizontalPadding = 10.dp
private val DogInfoEditTopBarSideMinWidth = 48.dp
private val DogInfoEditBackButtonSize = 44.dp
private val DogInfoEditSaveEndPadding = 16.dp
private val DogInfoEditPetIconAreaTopPadding = 24.dp
private val DogInfoEditPetIconCircleSize = 210.dp
private val DogInfoEditPetIconInnerPadding = 4.dp
private val DogInfoEditIconSectionHorizontalPadding = 24.dp
private val DogInfoEditIconSectionTopPadding = 20.dp
private val DogInfoEditIconLabelToPickerTopPadding = 8.dp
private const val DogInfoEditIconPickerColumnCount = 4
private val DogInfoEditIconPickerItemSize = 70.dp
private val DogInfoEditIconPickerItemInnerPadding = 2.dp
private val DogInfoEditIconPickerUnselectedBorderWidth = 0.4.dp
private val DogInfoEditIconPickerSelectedBorderWidth = 2.5.dp
private val DogInfoEditFormSectionTopPadding = 20.dp
private val DogInfoEditFormFieldGroupTopPadding = 20.dp
private val DogInfoEditFormLabelToFieldTopPadding = 8.dp
private val DogInfoEditFormFieldCornerRadius = 12.dp
private val DogInfoEditFormFieldBorderWidth = 1.dp
private val DogInfoEditFormFieldContentPadding = 14.dp
private val DogInfoEditScrollBottomPadding = 24.dp

@Composable
fun DogInfoEditScreen(
    @DrawableRes petIconRes: Int = R.drawable.ic_dog1,
    petName: String = "내새꾸",
    adoptionDate: LocalDate = todayLocalDate(),
    selectablePetIconRes: List<Int> = DogInfoEditViewModel.SELECTABLE_PET_ICON_RES,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onPetNameChange: (String) -> Unit = {},
    onPetIconSelect: (Int) -> Unit = {},
    isDatePickerVisible: Boolean = false,
    onCalendarClick: () -> Unit = {},
    onDatePickerDismiss: () -> Unit = {},
    onAdoptionDateSelected: (LocalDate) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val topBarTextStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.black),
    )

    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxSize()) {
        DogInfoEditTitleBar(
            topBarTextStyle = topBarTextStyle,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(bottom = DogInfoEditScrollBottomPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
            DogInfoEditPetFormSection(
                topBarTextStyle = topBarTextStyle,
                petName = petName,
                adoptionDate = adoptionDate,
                onPetNameChange = onPetNameChange,
                onCalendarClick = onCalendarClick,
            )
        }
    }

    if (isDatePickerVisible) {
        PastOrTodayDatePickerDialog(
            initialDate = adoptionDate,
            onDismissRequest = onDatePickerDismiss,
            onCancel = onDatePickerDismiss,
            onConfirm = onAdoptionDateSelected,
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
            val itemSize = minOf(
                DogInfoEditIconPickerItemSize,
                maxWidth / DogInfoEditIconPickerColumnCount,
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                selectablePetIconRes.chunked(DogInfoEditIconPickerColumnCount).forEach { rowIcons ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowIcons.forEach { iconRes ->
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center,
                            ) {
                                DogInfoEditIconPickerItem(
                                    iconRes = iconRes,
                                    itemSize = itemSize,
                                    isSelected = iconRes == selectedPetIconRes,
                                    onClick = { onPetIconSelect(iconRes) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DogInfoEditPetFormSection(
    topBarTextStyle: TextStyle,
    petName: String,
    adoptionDate: LocalDate,
    onPetNameChange: (String) -> Unit,
    onCalendarClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = DogInfoEditFormSectionTopPadding,
                start = DogInfoEditIconSectionHorizontalPadding,
                end = DogInfoEditIconSectionHorizontalPadding,
            ),
    ) {
        DogInfoEditLabeledTextField(
            label = stringResource(R.string.dog_info_edit_name_label),
            value = petName,
            onValueChange = onPetNameChange,
            labelStyle = topBarTextStyle,
        )
        DogInfoEditLabeledAdoptionDateField(
            label = stringResource(R.string.dog_info_edit_adoption_date_label),
            adoptionDate = adoptionDate,
            onCalendarClick = onCalendarClick,
            labelStyle = topBarTextStyle,
            modifier = Modifier.padding(top = DogInfoEditFormFieldGroupTopPadding),
        )
    }
}

@Composable
private fun DogInfoEditLabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    labelStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    DogInfoEditLabeledField(
        label = label,
        labelStyle = labelStyle,
        modifier = modifier,
    ) {
        DogInfoEditBorderedTextField(
            value = value,
            onValueChange = onValueChange,
        )
    }
}

@Composable
private fun DogInfoEditLabeledAdoptionDateField(
    label: String,
    adoptionDate: LocalDate,
    onCalendarClick: () -> Unit,
    labelStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    DogInfoEditLabeledField(
        label = label,
        labelStyle = labelStyle,
        modifier = modifier,
    ) {
        DogInfoEditAdoptionDateField(
            adoptionDate = adoptionDate,
            onCalendarClick = onCalendarClick,
        )
    }
}

@Composable
private fun DogInfoEditLabeledField(
    label: String,
    labelStyle: TextStyle,
    modifier: Modifier = Modifier,
    field: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            style = labelStyle,
        )
        Box(modifier = Modifier.padding(top = DogInfoEditFormLabelToFieldTopPadding)) {
            field()
        }
    }
}

@Composable
private fun DogInfoEditFormFieldTextStyle(): TextStyle {
    return MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Normal,
        color = colorResource(R.color.black),
        fontSize = 18.sp,
        lineHeight = 24.sp,
    )
}

@Composable
private fun DogInfoEditBorderedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val fieldShape = RoundedCornerShape(DogInfoEditFormFieldCornerRadius)
    val fieldTextStyle = DogInfoEditFormFieldTextStyle()
    val fieldBorderColor = colorResource(R.color.black)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = DogInfoEditFormFieldBorderWidth,
                color = fieldBorderColor,
                shape = fieldShape,
            )
            .padding(DogInfoEditFormFieldContentPadding),
        textStyle = fieldTextStyle,
        cursorBrush = SolidColor(fieldBorderColor),
        singleLine = true,
    )
}

@Composable
private fun DogInfoEditAdoptionDateField(
    adoptionDate: LocalDate,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fieldShape = RoundedCornerShape(DogInfoEditFormFieldCornerRadius)
    val fieldTextStyle = DogInfoEditFormFieldTextStyle()
    val fieldBorderColor = colorResource(R.color.black)
    val calendarIconHeight = with(LocalDensity.current) {
        fieldTextStyle.lineHeight.toDp()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = DogInfoEditFormFieldBorderWidth,
                color = fieldBorderColor,
                shape = fieldShape,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = adoptionDate.formatDotSeparated(),
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = DogInfoEditFormFieldContentPadding,
                    top = DogInfoEditFormFieldContentPadding,
                    bottom = DogInfoEditFormFieldContentPadding,
                ),
            style = fieldTextStyle,
        )
        DogInfoEditCalendarButton(
            onClick = onCalendarClick,
            iconHeight = calendarIconHeight,
            modifier = Modifier.padding(DogInfoEditFormFieldContentPadding),
        )
    }
}

@Composable
private fun DogInfoEditCalendarButton(
    onClick: () -> Unit,
    iconHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
        ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = null,
            modifier = Modifier.height(iconHeight),
            contentScale = ContentScale.Fit,
        )
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
            color = colorResource(R.color.slate),
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
        DogInfoEditScreen(
            petName = "내새꾸",
        )
    }
}
