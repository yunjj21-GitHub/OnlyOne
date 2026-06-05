package com.yjp.onlyone.presentation.home

import com.yjp.onlyone.domain.model.HomeHappinessInput

/**
 * 행복 지수 Picker 옵션·표시 문자열·선택값 매핑.
 */
object HomeHappinessPickerConfig {
    private val MEAL_COUNTS = intArrayOf(0, 1, 2)
    val MEAL_LABELS: Array<String> = arrayOf("0회", "1회", "2회")

    val DURATION_MINUTES: IntArray = intArrayOf(0, 10, 20, 30, 40, 50, 60)
    val DURATION_LABELS: Array<String> = arrayOf("0분", "10분", "20분", "30분", "40분", "50분", "60분")

    private val SNACK_COUNTS = intArrayOf(0, 1, 2, 3)
    val SNACK_LABELS: Array<String> = arrayOf("0회", "1회", "2회", "3회")

    /** index 0 = X, 1 = O(만점) */
    val TEETH_LABELS: Array<String> = arrayOf("X", "O")

    fun titleFor(type: HomeActivityType): String = when (type) {
        HomeActivityType.MEAL -> "밥"
        HomeActivityType.WALK -> "산책"
        HomeActivityType.PLAY -> "놀이"
        HomeActivityType.SNACK -> "간식"
        HomeActivityType.TEETH -> "양치"
    }

    fun labelsFor(type: HomeActivityType): List<String> = when (type) {
        HomeActivityType.MEAL -> MEAL_LABELS.toList()
        HomeActivityType.WALK,
        HomeActivityType.PLAY,
        -> DURATION_LABELS.toList()
        HomeActivityType.SNACK -> SNACK_LABELS.toList()
        HomeActivityType.TEETH -> TEETH_LABELS.toList()
    }

    fun initialIndex(type: HomeActivityType, input: HomeHappinessInput): Int = when (type) {
        HomeActivityType.MEAL -> indexOfOrZero(MEAL_COUNTS, input.mealCount)
        HomeActivityType.WALK -> indexForDurationMinutes(input.walkTotalMinutes)
        HomeActivityType.PLAY -> indexForDurationMinutes(input.playTotalMinutes)
        HomeActivityType.SNACK -> indexOfOrZero(SNACK_COUNTS, input.snackCount)
        HomeActivityType.TEETH -> if (input.teethBrushed) 1 else 0
    }

    fun applySelection(
        type: HomeActivityType,
        input: HomeHappinessInput,
        selectedIndex: Int,
    ): HomeHappinessInput {
        return when (type) {
            HomeActivityType.MEAL -> input.copy(
                mealCount = valueAtIndex(MEAL_COUNTS, selectedIndex),
            )
            HomeActivityType.WALK -> input.copy(
                walkTotalMinutes = durationMinutesAt(selectedIndex),
            )
            HomeActivityType.PLAY -> input.copy(
                playTotalMinutes = durationMinutesAt(selectedIndex),
            )
            HomeActivityType.SNACK -> input.copy(
                snackCount = valueAtIndex(SNACK_COUNTS, selectedIndex),
            )
            HomeActivityType.TEETH -> input.copy(
                teethBrushed = selectedIndex == 1,
            )
        }
    }

    fun mealDisplayLabel(count: Int): String = labelAt(MEAL_LABELS, MEAL_COUNTS, count)

    fun snackDisplayLabel(count: Int): String = labelAt(SNACK_LABELS, SNACK_COUNTS, count)

    fun durationDisplayLabel(minutes: Int): String {
        if (minutes <= 0) return "0분"
        val index = DURATION_MINUTES.indexOf(minutes)
        return if (index >= 0) DURATION_LABELS[index] else "0분"
    }

    fun teethDisplayLabel(brushed: Boolean): String = if (brushed) TEETH_LABELS[1] else TEETH_LABELS[0]

    private fun indexForDurationMinutes(minutes: Int): Int =
        indexOfOrZero(DURATION_MINUTES, minutes.coerceAtLeast(0))

    private fun durationMinutesAt(index: Int): Int = valueAtIndex(DURATION_MINUTES, index)

    private fun indexOfOrZero(values: IntArray, value: Int): Int {
        val index = values.indexOf(value)
        return if (index >= 0) index else 0
    }

    private fun valueAtIndex(values: IntArray, index: Int): Int {
        return values[index.coerceIn(0, values.lastIndex)]
    }

    private fun labelAt(labels: Array<String>, values: IntArray, value: Int): String {
        val index = values.indexOf(value)
        return if (index >= 0) labels[index] else labels[0]
    }
}
