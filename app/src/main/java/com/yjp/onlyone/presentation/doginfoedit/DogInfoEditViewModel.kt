package com.yjp.onlyone.presentation.doginfoedit

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjp.onlyone.R
import com.yjp.onlyone.domain.model.PetInfo
import com.yjp.onlyone.domain.repository.PetRepository
import com.yjp.onlyone.util.todayLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
class DogInfoEditViewModel @Inject constructor(
    private val petRepository: PetRepository,
    @ApplicationContext context: Context,
) : ViewModel() {

    @DrawableRes
    private val _petIconRes = MutableStateFlow(R.drawable.ic_dog1)
    val petIconRes: StateFlow<Int> = _petIconRes.asStateFlow()

    private val _petName = MutableStateFlow(context.getString(R.string.pet_name_default))
    val petName: StateFlow<String> = _petName.asStateFlow()

    private val _adoptionDate = MutableStateFlow(todayLocalDate())
    val adoptionDate: StateFlow<LocalDate> = _adoptionDate.asStateFlow()

    private val _isDatePickerVisible = MutableStateFlow(false)
    val isDatePickerVisible: StateFlow<Boolean> = _isDatePickerVisible.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<DogInfoEditNavigation>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<DogInfoEditNavigation> = _navigationEvent.asSharedFlow()

    private val _discardAlertRequest = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val discardAlertRequest: SharedFlow<Unit> = _discardAlertRequest.asSharedFlow()

    private val _saveToastEvent = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val saveToastEvent: SharedFlow<Boolean> = _saveToastEvent.asSharedFlow()

    private var savedPetInfo: PetInfo? = null

    init {
        loadPetInfo()
    }

    private fun loadPetInfo() {
        viewModelScope.launch {
            applyPetInfo(petRepository.getPetInfo())
        }
    }

    private fun applyPetInfo(petInfo: PetInfo) {
        _petIconRes.value = petInfo.iconRes
        _petName.value = petInfo.name
        _adoptionDate.value = petInfo.adoptionDate
        savedPetInfo = petInfo
    }

    fun onBackClick() {
        if (_isDatePickerVisible.value) {
            _isDatePickerVisible.value = false
            return
        }
        if (hasUnsavedChanges()) {
            _discardAlertRequest.tryEmit(Unit)
        } else {
            navigateHome()
        }
    }

    fun onDiscardConfirmed() {
        navigateHome()
    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (!hasUnsavedChanges()) {
                _saveToastEvent.emit(false)
                return@launch
            }
            val petInfo = currentPetInfo()
            petRepository.savePetInfo(petInfo)
            savedPetInfo = petInfo
            _saveToastEvent.emit(true)
        }
    }

    fun onPetNameChange(name: String) {
        _petName.value = name
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

    private fun currentPetInfo(): PetInfo {
        return PetInfo(
            iconRes = _petIconRes.value,
            name = _petName.value,
            adoptionDate = _adoptionDate.value,
        )
    }

    private fun hasUnsavedChanges(): Boolean {
        val saved = savedPetInfo ?: return false
        return currentPetInfo() != saved
    }

    private fun navigateHome() {
        viewModelScope.launch {
            _navigationEvent.emit(DogInfoEditNavigation.ToHome)
        }
    }

    companion object {
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
