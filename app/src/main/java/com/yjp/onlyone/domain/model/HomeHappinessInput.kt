package com.yjp.onlyone.domain.model

/**
 * 홈 행복 지수 카드 5개 항목 입력값.
 *
 * 점수·만점·Picker 규칙은 [com.yjp.onlyone.domain.happiness.HappinessIndexCalculator],
 * [com.yjp.onlyone.presentation.home.HomeHappinessPickerConfig] 참고
 */
data class HomeHappinessInput(
    val mealCount: Int = 0,
    val walkTotalMinutes: Int = 0,
    val playTotalMinutes: Int = 0,
    val snackCount: Int = 0,
    val teethBrushed: Boolean = false,
)
