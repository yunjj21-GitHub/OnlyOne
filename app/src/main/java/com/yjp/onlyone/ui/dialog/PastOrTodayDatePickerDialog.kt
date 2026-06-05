package com.yjp.onlyone.ui.dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yjp.onlyone.R
import com.yjp.onlyone.ui.theme.OnlyOneTheme
import com.yjp.onlyone.util.todayLocalDate
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val WeekdayLabels = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
private val MonthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)

@Composable
fun PastOrTodayDatePickerDialog(
    initialDate: LocalDate,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        PastOrTodayDatePickerContent(
            initialDate = initialDate,
            onCancel = onCancel,
            onConfirm = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DatePickerDialogHorizontalMargin),
        )
    }
}

@Composable
private fun PastOrTodayDatePickerContent(
    initialDate: LocalDate,
    onCancel: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = remember { todayLocalDate() }
    var displayedMonth by remember(initialDate) { mutableStateOf(YearMonth.from(initialDate)) }
    val monthCells = remember(displayedMonth) { buildMonthGrid(displayedMonth) }
    val canGoNextMonth = displayedMonth < YearMonth.from(today)

    val white = colorResource(R.color.white)
    val black = colorResource(R.color.black)
    val muted = colorResource(R.color.bone_muted)
    val selectedColor = colorResource(R.color.slate)
    val headerStyle = datePickerHeaderTextStyle()
    val weekdayStyle = datePickerWeekdayTextStyle()
    val dayStyle = datePickerDayTextStyle()

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(DatePickerDialogCornerRadius),
        color = white,
    ) {
        Column(
            modifier = Modifier.padding(DatePickerDialogPadding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DatePickerNavButton(
                    iconRes = R.drawable.ic_double_left,
                    onClick = { displayedMonth = displayedMonth.minusMonths(1) },
                    contentDescription = "",
                )
                Text(
                    text = displayedMonth.format(MonthYearFormatter),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    style = headerStyle,
                    color = black,
                    textAlign = TextAlign.Center,
                )
                DatePickerNavButton(
                    iconRes = R.drawable.ic_double_right,
                    onClick = { displayedMonth = displayedMonth.plusMonths(1) },
                    enabled = canGoNextMonth,
                    contentDescription = "",
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                WeekdayLabels.forEach { label ->
                    Text(
                        text = label,
                        modifier = Modifier.weight(1f),
                        style = weekdayStyle,
                        color = black,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                monthCells.chunked(7).forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        week.forEach { cell ->
                            val isSelectable = !cell.date.isAfter(today)
                            val isSelected = cell.date == initialDate
                            val isToday = cell.date == today

                            DatePickerDayCell(
                                day = cell.date.dayOfMonth,
                                isCurrentMonth = cell.isCurrentMonth,
                                isSelectable = isSelectable,
                                isSelected = isSelected,
                                isToday = isToday,
                                dayStyle = dayStyle,
                                selectedColor = selectedColor,
                                black = black,
                                muted = muted,
                                white = white,
                                onClick = { onConfirm(cell.date) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DatePickerNavButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    contentDescription: String,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(36.dp),
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(14.dp)
                .alpha(if (enabled) 1f else 0.38f),
        )
    }
}

@Composable
private fun DatePickerDayCell(
    day: Int,
    isCurrentMonth: Boolean,
    isSelectable: Boolean,
    isSelected: Boolean,
    isToday: Boolean,
    dayStyle: TextStyle,
    selectedColor: Color,
    black: Color,
    muted: Color,
    white: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val showTodayBorder = isToday && isSelectable && !isSelected
    val backgroundColor = if (isSelected) selectedColor else Color.Transparent
    val textColor = when {
        isSelected -> white
        !isCurrentMonth || !isSelectable -> muted
        else -> black
    }

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(DatePickerDayFillFraction)
                .clip(CircleShape)
                .background(backgroundColor)
                .then(
                    if (showTodayBorder) {
                        Modifier.border(2.dp, selectedColor, CircleShape)
                    } else {
                        Modifier
                    },
                )
                .then(
                    if (isSelectable) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = day.toString(),
                style = dayStyle,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private data class CalendarDayCell(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
)

private fun buildMonthGrid(displayedMonth: YearMonth): List<CalendarDayCell> {
    val firstOfMonth = displayedMonth.atDay(1)
    val startOffset = firstOfMonth.dayOfWeek.value % 7
    val gridStart = firstOfMonth.minusDays(startOffset.toLong())
    return (0 until 42).map { index ->
        val date = gridStart.plusDays(index.toLong())
        CalendarDayCell(
            date = date,
            isCurrentMonth = YearMonth.from(date) == displayedMonth,
        )
    }
}

@Composable
private fun datePickerHeaderTextStyle(): TextStyle =
    MaterialTheme.typography.bodyLarge.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
    )

@Composable
private fun datePickerWeekdayTextStyle(): TextStyle =
    MaterialTheme.typography.bodyMedium.copy(
        fontSize = 18.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
    )

@Composable
private fun datePickerDayTextStyle(): TextStyle =
    MaterialTheme.typography.bodyLarge.copy(
        fontSize = 18.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium,
    )

private val DatePickerDialogHorizontalMargin = 24.dp
private val DatePickerDialogPadding = 8.dp
private val DatePickerDialogCornerRadius = 10.dp
private val DatePickerDayCornerRadius = 10.dp
private const val DatePickerDayFillFraction = 0.86f

@Preview(showBackground = true, backgroundColor = 0xFFEAEAEA)
@Composable
private fun PastOrTodayDatePickerDialogPreview() {
    OnlyOneTheme {
        PastOrTodayDatePickerContent(
            initialDate = todayLocalDate(),
            onCancel = {},
            onConfirm = {},
        )
    }
}
