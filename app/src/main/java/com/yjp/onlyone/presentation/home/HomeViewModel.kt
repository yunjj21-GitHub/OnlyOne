package com.yjp.onlyone.presentation.home

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.yjp.onlyone.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _petName = MutableStateFlow(DEFAULT_PET_NAME)
    val petName: StateFlow<String> = _petName.asStateFlow()

    @DrawableRes
    private val _petIconRes = MutableStateFlow(DEFAULT_PET_ICON_RES)
    val petIconRes: StateFlow<Int> = _petIconRes.asStateFlow()

    private val _happinessIndex = MutableStateFlow(DEFAULT_HAPPINESS_INDEX)
    val happinessIndex: StateFlow<Int> = _happinessIndex.asStateFlow()

    private val _daysTogether = MutableStateFlow(DEFAULT_DAYS_TOGETHER)
    val daysTogether: StateFlow<Int> = _daysTogether.asStateFlow()

    fun updatePetName(name: String) {
        _petName.value = name
    }

    fun updatePetIcon(@DrawableRes iconRes: Int) {
        _petIconRes.value = iconRes
    }

    fun updateHappinessIndex(index: Int) {
        _happinessIndex.value = index.coerceIn(HAPPINESS_INDEX_MIN, HAPPINESS_INDEX_MAX)
    }

    fun updateDaysTogether(days: Int) {
        _daysTogether.value = days.coerceAtLeast(0)
    }

    companion object {
        const val HAPPINESS_INDEX_MIN = 0
        const val HAPPINESS_INDEX_MAX = 100
        const val DEFAULT_PET_NAME = "내새꾸"
        const val DEFAULT_HAPPINESS_INDEX = 55
        const val DEFAULT_DAYS_TOGETHER = 1004

        @DrawableRes
        val DEFAULT_PET_ICON_RES: Int = R.drawable.ic_dog1

        fun happinessProgress(index: Int): Float {
            return index.coerceIn(HAPPINESS_INDEX_MIN, HAPPINESS_INDEX_MAX) / 100f
        }

        fun formatDaysTogether(days: Int): String {
            return if (days >= 1000) {
                NumberFormat.getNumberInstance(Locale.KOREA).format(days)
            } else {
                days.toString()
            }
        }

        fun buildTogetherDaysText(days: Int): String {
            val formattedDays = formatDaysTogether(days)
            return "♥ 우리가 함께한지 ${formattedDays}일 째 ♥"
        }
    }
}
