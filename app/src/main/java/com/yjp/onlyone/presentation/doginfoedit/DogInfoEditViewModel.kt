package com.yjp.onlyone.presentation.doginfoedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogInfoEditViewModel @Inject constructor() : ViewModel() {

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
