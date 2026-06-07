package com.yjp.onlyone.util

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

/** 위·경도를 기상청 단기예보 격자 좌표(nx, ny)로 변환한다. */
object KmaGridConverter {

    /** 위치 조회 실패 시 사용하는 서울 기본 격자. */
    const val FALLBACK_NX = 60
    const val FALLBACK_NY = 127
    const val FALLBACK_LATITUDE = 37.5665
    const val FALLBACK_LONGITUDE = 126.9780

    fun toGrid(latitude: Double, longitude: Double): Pair<Int, Int> {
        val re = EARTH_RADIUS_KM / GRID_SPACING_KM
        val slat1 = STANDARD_PARALLEL_1 * DEG_TO_RAD
        val slat2 = STANDARD_PARALLEL_2 * DEG_TO_RAD
        val olon = REFERENCE_LONGITUDE * DEG_TO_RAD
        val olat = REFERENCE_LATITUDE * DEG_TO_RAD

        val sn = ln(cos(slat1) / cos(slat2)) /
            ln(tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5))
        val sf = tan(PI * 0.25 + slat1 * 0.5).pow(sn) * cos(slat1) / sn
        val ro = re * sf / tan(PI * 0.25 + olat * 0.5).pow(sn)

        val ra = re * sf / tan(PI * 0.25 + latitude * DEG_TO_RAD * 0.5).pow(sn)
        var theta = longitude * DEG_TO_RAD - olon
        if (theta > PI) theta -= 2.0 * PI
        if (theta < -PI) theta += 2.0 * PI
        theta *= sn

        val nx = floor(ra * sin(theta) + GRID_ORIGIN_X + 0.5).toInt()
        val ny = floor(ro - ra * cos(theta) + GRID_ORIGIN_Y + 0.5).toInt()
        return nx to ny
    }

    private const val EARTH_RADIUS_KM = 6371.00877
    private const val GRID_SPACING_KM = 5.0
    private const val STANDARD_PARALLEL_1 = 30.0
    private const val STANDARD_PARALLEL_2 = 60.0
    private const val REFERENCE_LONGITUDE = 126.0
    private const val REFERENCE_LATITUDE = 38.0
    private const val GRID_ORIGIN_X = 43.0
    private const val GRID_ORIGIN_Y = 136.0
    private const val DEG_TO_RAD = PI / 180.0
}
