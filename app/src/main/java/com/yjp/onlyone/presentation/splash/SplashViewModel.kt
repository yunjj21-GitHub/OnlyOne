package com.yjp.onlyone.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<SplashNavigation>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<SplashNavigation> = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(SPLASH_DELAY_MILLIS)
            _navigationEvent.emit(SplashNavigation.ToHome)
        }
    }

    companion object {
        private const val SPLASH_DELAY_MILLIS = 2_000L
    }
}
