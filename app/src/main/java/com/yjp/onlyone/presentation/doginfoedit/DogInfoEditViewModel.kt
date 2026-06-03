package com.yjp.onlyone.presentation.doginfoedit

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjp.onlyone.presentation.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogInfoEditViewModel @Inject constructor() : ViewModel() {

    @DrawableRes
    private val _petIconRes = MutableStateFlow(HomeViewModel.DEFAULT_PET_ICON_RES)
    val petIconRes: StateFlow<Int> = _petIconRes.asStateFlow()

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
}
