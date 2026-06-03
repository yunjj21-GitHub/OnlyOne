package com.yjp.onlyone.presentation.memo

sealed interface MemoNavigation {
    data object ToHome : MemoNavigation
}
