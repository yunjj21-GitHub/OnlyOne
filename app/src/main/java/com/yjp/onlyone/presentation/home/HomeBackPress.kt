package com.yjp.onlyone.presentation.home

sealed interface HomeBackPress {
    data object ShowExitToast : HomeBackPress
    data object FinishApp : HomeBackPress
}
