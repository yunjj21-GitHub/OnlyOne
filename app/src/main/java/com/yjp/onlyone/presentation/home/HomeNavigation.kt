package com.yjp.onlyone.presentation.home

sealed interface HomeNavigation {
    data object ToMemo : HomeNavigation
    data object ToDogInfoEdit : HomeNavigation
}
