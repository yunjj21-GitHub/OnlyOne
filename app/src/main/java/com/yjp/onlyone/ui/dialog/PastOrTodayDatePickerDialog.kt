package com.yjp.onlyone.ui.dialog

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.yjp.onlyone.R
import com.yjp.onlyone.util.PastOrTodaySelectableDates
import com.yjp.onlyone.util.toLocalDateFromUtcPickerMillis
import com.yjp.onlyone.util.toUtcPickerMillis
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastOrTodayDatePickerDialog(
    initialDate: LocalDate,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.toUtcPickerMillis(),
        selectableDates = PastOrTodaySelectableDates,
    )
    val datePickerScrollState = rememberScrollState()
    val dialogButtonColors = ButtonDefaults.textButtonColors(
        contentColor = colorResource(R.color.black),
    )
    val datePickerColors = pastOrTodayDatePickerColors()

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis
                        ?.toLocalDateFromUtcPickerMillis()
                        ?.let(onConfirm)
                },
                colors = dialogButtonColors,
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
                colors = dialogButtonColors,
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier.verticalScroll(datePickerScrollState),
            showModeToggle = false,
            colors = datePickerColors,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun pastOrTodayDatePickerColors() = DatePickerDefaults.colors(
    titleContentColor = colorResource(R.color.black),
    headlineContentColor = colorResource(R.color.black),
    weekdayContentColor = colorResource(R.color.black),
    subheadContentColor = colorResource(R.color.black),
    navigationContentColor = colorResource(R.color.black),
    yearContentColor = colorResource(R.color.black),
    currentYearContentColor = colorResource(R.color.black),
    selectedYearContainerColor = colorResource(R.color.black),
    selectedYearContentColor = colorResource(R.color.white),
    dayContentColor = colorResource(R.color.black),
    selectedDayContainerColor = colorResource(R.color.black),
    selectedDayContentColor = colorResource(R.color.white),
    todayContentColor = colorResource(R.color.black),
    todayDateBorderColor = colorResource(R.color.black),
    dayInSelectionRangeContainerColor = colorResource(R.color.black),
    dayInSelectionRangeContentColor = colorResource(R.color.white),
    dividerColor = colorResource(R.color.black),
)
