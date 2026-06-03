package com.yjp.onlyone.presentation.memo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<MemoNavigation>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<MemoNavigation> = _navigationEvent.asSharedFlow()

    fun onBackClick() {
        viewModelScope.launch {
            _navigationEvent.emit(MemoNavigation.ToHome)
        }
    }
}
