package com.yjp.onlyone.presentation.doginfoedit

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjp.onlyone.R
import com.yjp.onlyone.presentation.home.HomeViewModel
import com.yjp.onlyone.util.localDateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DogInfoEditViewModel @Inject constructor() : ViewModel() {

    @DrawableRes
    private val _petIconRes = MutableStateFlow(HomeViewModel.DEFAULT_PET_ICON_RES)
    val petIconRes: StateFlow<Int> = _petIconRes.asStateFlow()

    private val _petName = MutableStateFlow(HomeViewModel.DEFAULT_PET_NAME)
    val petName: StateFlow<String> = _petName.asStateFlow()

    private val _adoptionDate = MutableStateFlow(DEFAULT_ADOPTION_DATE)
    val adoptionDate: StateFlow<LocalDate> = _adoptionDate.asStateFlow()

    private val _isDatePickerVisible = MutableStateFlow(false)
    val isDatePickerVisible: StateFlow<Boolean> = _isDatePickerVisible.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<DogInfoEditNavigation>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<DogInfoEditNavigation> = _navigationEvent.asSharedFlow()

    fun onBackClick() {
        viewModelScope.launch {
            _navigationEvent.emit(DogInfoEditNavigation.ToHome)
        }
    }

    fun onSaveClick() {
        // TODO: persist dog info
    }

    fun onPetIconSelect(@DrawableRes iconRes: Int) {
        if (iconRes in SELECTABLE_PET_ICON_RES) {
            _petIconRes.value = iconRes
        }
    }

    fun onCalendarClick() {
        _isDatePickerVisible.value = true
    }

    fun onDatePickerDismiss() {
        _isDatePickerVisible.value = false
    }

    fun onAdoptionDateSelected(date: LocalDate) {
        _adoptionDate.value = date
        _isDatePickerVisible.value = false
    }

    companion object {
        val DEFAULT_ADOPTION_DATE: LocalDate = localDateOf(2019, 2, 4)

        val SELECTABLE_PET_ICON_RES: List<Int> = listOf(
            R.drawable.ic_dog1,
            R.drawable.ic_dog19,
            R.drawable.ic_dog18,
            R.drawable.ic_dog14,
            R.drawable.ic_dog13,
            R.drawable.ic_dog11,
            R.drawable.ic_dog12,
            R.drawable.ic_dog17,
            R.drawable.ic_dog2,
            R.drawable.ic_dog10,
            R.drawable.ic_dog8,
            R.drawable.ic_dog20,
        )
    }
}
