package com.yjp.onlyone.domain.happiness

import com.yjp.onlyone.domain.model.HomeHappinessInput
import kotlin.math.roundToInt

/**
 * 홈 화면 행복 지수 정책
 *
 * ## 총점
 * - 5개 항목 합산, 100점 만점, 초기값(아무 것도 안 함)은 0점
 * - 항목당 최대 20점
 *
 * ## Picker 리스트 ([HomeHappinessPickerConfig])
 * - 밥: 0회, 1회, 2회 (2회 만점)
 * - 산책: 0분~60분 (60분 만점)
 * - 놀이: 0분~60분 (60분 만점)
 * - 간식: 0회, 1회, 2회, 3회 (3회 만점)
 * - 양치: X, O (O 만점)
 *
 * ## 행복 지수 점수 (만점이 아니어도 비례 반영)
 * - 밥·산책·놀이·간식: [실제 값 / 만점 기준] × 20 (반올림), 0이면 0점
 * - 양치: O = 20점, X = 0점
 *
 * | 항목 | 만점(20점) | 부분 점수 예시 |
 * |------|-----------|----------------|
 * | 밥 | 2회 | 0회→0, 1회→10 |
 * | 산책 | 60분 | 0분→0, 30분→10, 10분→3 |
 * | 놀이 | 60분 | 0분→0, 30분→10 |
 * | 간식 | 3회 | 1회→7, 2회→13 |
 * | 양치 | O | X→0 |
 *
 * ## 뼈다귀 아이콘 (만점 = ic_bone_slate, 미만 = ic_bone_muted)
 * - 밥 2회, 산책 60분, 놀이 60분, 간식 3회, 양치 O
 */
object HappinessIndexCalculator {
    private const val POINTS_PER_ITEM = 20
    private const val MEAL_FULL_SCORE_COUNT = 2
    private const val DURATION_FULL_SCORE_MINUTES = 60
    private const val SNACK_FULL_SCORE_COUNT = 3

    fun calculate(input: HomeHappinessInput): Int {
        return (
            mealPoints(input.mealCount) +
                walkPoints(input.walkTotalMinutes) +
                playPoints(input.playTotalMinutes) +
                snackPoints(input.snackCount) +
                teethPoints(input.teethBrushed)
            ).coerceIn(0, 100)
    }

    fun mealPoints(mealCount: Int): Int = proportionalPoints(
        actual = mealCount.coerceIn(0, MEAL_FULL_SCORE_COUNT),
        fullThreshold = MEAL_FULL_SCORE_COUNT,
    )

    fun walkPoints(walkTotalMinutes: Int): Int = durationPoints(walkTotalMinutes)

    fun playPoints(playTotalMinutes: Int): Int = durationPoints(playTotalMinutes)

    fun snackPoints(snackCount: Int): Int = proportionalPoints(
        actual = snackCount.coerceIn(0, SNACK_FULL_SCORE_COUNT),
        fullThreshold = SNACK_FULL_SCORE_COUNT,
    )

    fun teethPoints(teethBrushed: Boolean): Int = if (teethBrushed) POINTS_PER_ITEM else 0

    fun isMealMax(mealCount: Int): Boolean = mealCount >= MEAL_FULL_SCORE_COUNT

    fun isWalkMax(walkTotalMinutes: Int): Boolean = walkTotalMinutes >= DURATION_FULL_SCORE_MINUTES

    fun isPlayMax(playTotalMinutes: Int): Boolean = playTotalMinutes >= DURATION_FULL_SCORE_MINUTES

    fun isSnackMax(snackCount: Int): Boolean = snackCount >= SNACK_FULL_SCORE_COUNT

    fun isTeethMax(teethBrushed: Boolean): Boolean = teethBrushed

    private fun durationPoints(minutes: Int): Int = proportionalPoints(
        actual = minutes.coerceAtLeast(0).coerceAtMost(DURATION_FULL_SCORE_MINUTES),
        fullThreshold = DURATION_FULL_SCORE_MINUTES,
    )

    private fun proportionalPoints(actual: Int, fullThreshold: Int): Int {
        if (fullThreshold <= 0 || actual <= 0) return 0
        val ratio = actual.coerceAtMost(fullThreshold).toFloat() / fullThreshold
        return (ratio * POINTS_PER_ITEM).roundToInt()
    }
}
