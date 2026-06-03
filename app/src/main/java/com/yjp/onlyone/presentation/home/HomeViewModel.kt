package com.yjp.onlyone.presentation.home

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.yjp.onlyone.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _petName = MutableStateFlow(DEFAULT_PET_NAME)
    val petName: StateFlow<String> = _petName.asStateFlow()

    @DrawableRes
    private val _petIconRes = MutableStateFlow(DEFAULT_PET_ICON_RES)
    val petIconRes: StateFlow<Int> = _petIconRes.asStateFlow()

    private val _happinessProgress = MutableStateFlow(DEFAULT_HAPPINESS_PROGRESS)
    val happinessProgress: StateFlow<Float> = _happinessProgress.asStateFlow()

    fun updatePetName(name: String) {
        _petName.value = name
    }

    fun updatePetIcon(@DrawableRes iconRes: Int) {
        _petIconRes.value = iconRes
    }

    fun updateHappinessProgress(progress: Float) {
        _happinessProgress.value = progress.coerceIn(0f, 1f)
    }

    companion object {
        const val DEFAULT_PET_NAME = "내새꾸"
        const val DEFAULT_HAPPINESS_PROGRESS = 0.55f

        @DrawableRes
        val DEFAULT_PET_ICON_RES: Int = R.drawable.ic_dog1
    }
}
