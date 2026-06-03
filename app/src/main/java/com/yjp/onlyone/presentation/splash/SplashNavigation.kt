package com.yjp.onlyone.presentation.splash

sealed interface SplashNavigation {
    data object ToHome : SplashNavigation
}
