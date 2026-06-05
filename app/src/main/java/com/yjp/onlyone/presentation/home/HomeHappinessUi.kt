package com.yjp.onlyone.presentation.home

import com.yjp.onlyone.domain.happiness.HappinessIndexCalculator
import com.yjp.onlyone.domain.model.HomeHappinessInput

enum class HomeActivityType {
    MEAL,
    WALK,
    PLAY,
    SNACK,
    TEETH,
}

data class HomeActivityStatUi(
    val type: HomeActivityType,
    val label: String,
    val valueText: String,
    val isMaxScore: Boolean,
)

sealed interface HomeHappinessPicker {
    data object None : HomeHappinessPicker

    data class Active(
        val type: HomeActivityType,
        val initialIndex: Int,
    ) : HomeHappinessPicker
}

object HomeHappinessUiMapper {

    fun buildActivityStats(input: HomeHappinessInput): List<HomeActivityStatUi> {
        return listOf(
            HomeActivityStatUi(
                type = HomeActivityType.MEAL,
                label = "밥",
                valueText = HomeHappinessPickerConfig.mealDisplayLabel(input.mealCount),
                isMaxScore = HappinessIndexCalculator.isMealMax(input.mealCount),
            ),
            HomeActivityStatUi(
                type = HomeActivityType.WALK,
                label = "산책",
                valueText = HomeHappinessPickerConfig.durationDisplayLabel(input.walkTotalMinutes),
                isMaxScore = HappinessIndexCalculator.isWalkMax(input.walkTotalMinutes),
            ),
            HomeActivityStatUi(
                type = HomeActivityType.PLAY,
                label = "놀이",
                valueText = HomeHappinessPickerConfig.durationDisplayLabel(input.playTotalMinutes),
                isMaxScore = HappinessIndexCalculator.isPlayMax(input.playTotalMinutes),
            ),
            HomeActivityStatUi(
                type = HomeActivityType.SNACK,
                label = "간식",
                valueText = HomeHappinessPickerConfig.snackDisplayLabel(input.snackCount),
                isMaxScore = HappinessIndexCalculator.isSnackMax(input.snackCount),
            ),
            HomeActivityStatUi(
                type = HomeActivityType.TEETH,
                label = "양치",
                valueText = HomeHappinessPickerConfig.teethDisplayLabel(input.teethBrushed),
                isMaxScore = HappinessIndexCalculator.isTeethMax(input.teethBrushed),
            ),
        )
    }
}
