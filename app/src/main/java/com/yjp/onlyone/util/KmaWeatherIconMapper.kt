package com.yjp.onlyone.util

import androidx.annotation.DrawableRes
import com.yjp.onlyone.R
import com.yjp.onlyone.domain.model.CurrentSkyWeather

/**
 * 기상청 초단기예보 코드를 날씨 아이콘으로 매핑한다.
 *
 * 사용 API: getUltraSrtFcst
 * - SKY: 하늘 상태 (1 맑음, 3 구름많음, 4 흐림)
 * - PTY: 강수 형태 (0 없음, 1 비, 2 비/눈, 3 눈, 5 빗방울, 6 빗방울눈날림, 7 눈낄림)
 * - LGT: 낙뢰 (0 없음, 0 초과 시 낙뢰 가능)
 *
 * 아이콘 우선순위: LGT → PTY(강수) → SKY(하늘) → 낮/밤
 * 낮/밤 판별은 [SunTimeUtil]에서 위·경도 기준 일출/일몰로 계산한다.
 * 한파·폭염·안개·토네이도·강풍 아이콘은 하늘 정보가 아니므로 여기서 매핑하지 않는다.
 */
object KmaWeatherIconMapper {

    /**
     * SKY / PTY / LGT 조합에 맞는 drawable 리소스를 반환한다.
     *
     * @param skyCode SKY 코드 (1, 3, 4)
     * @param precipitationCode PTY 코드
     * @param lightningValue LGT 값 (kA/㎢, 0 초과면 낙뢰)
     * @param isNight true면 맑음/구름많음 시 밤 아이콘 사용
     */
    @DrawableRes
    fun iconRes(
        skyCode: String?,
        precipitationCode: String?,
        lightningValue: String?,
        isNight: Boolean,
    ): Int {
        // 1) 낙뢰 — LGT가 PTY보다 우선
        if (hasLightning(lightningValue)) {
            if (hasRain(precipitationCode)) return R.drawable.ic_thunderstorm // ⛈️ 천둥번개+비
            return R.drawable.ic_lightning // 🌩️ 번개
        }

        // 2) 강수 — PTY가 있으면 SKY보다 우선
        when (precipitationCode) {
            "2", "6" -> return R.drawable.ic_sleet   // 🌨️🌧️ 진눈깨비
            "3", "7" -> return R.drawable.ic_snow    // 🌨️ 눈
            "1", "5", "4" -> return R.drawable.ic_rain // 🌧️ 비 (4=소나기, 단기예보용)
        }

        // 3) 하늘 — PTY=0일 때 SKY + 낮/밤
        return when (skyCode) {
            "1" -> if (isNight) R.drawable.ic_clear_night else R.drawable.ic_sunny           // 🌙 / ☀️
            "3" -> if (isNight) R.drawable.ic_partly_cloudy_night else R.drawable.ic_partly_cloudy // 🌙☁️ / ⛅
            "4" -> R.drawable.ic_cloudy // ☁️ 흐림
            else -> if (isNight) R.drawable.ic_clear_night else R.drawable.ic_sunny
        }
    }

    /** [iconRes]와 동일한 조건으로 화면에 표시할 날씨 문구를 반환한다. */
    fun weatherLabel(
        skyCode: String?,
        precipitationCode: String?,
        lightningValue: String?,
    ): String {
        if (hasLightning(lightningValue)) {
            return if (hasRain(precipitationCode)) "천둥번개" else "낙뢰"
        }
        return KmaWeatherCodeLabel.weather(
            skyCode = skyCode,
            precipitationCode = precipitationCode,
            shortTerm = true,
        )
    }

    /** 개발자 화면용 — 원본 코드, 일출/일몰, 예보 시각을 함께 표시한다. */
    fun detailLabel(weather: CurrentSkyWeather): String {
        val dayNight = if (weather.isNight) "밤" else "낮"
        return buildString {
            append("SKY=${weather.skyCode ?: "-"}")
            append(" PTY=${weather.precipitationCode ?: "-"}")
            append(" LGT=${weather.lightningValue ?: "-"}")
            append(" ($dayNight, 일출 ${weather.sunriseTime} 일몰 ${weather.sunsetTime})")
            append(" · ${weather.forecastDate} ${weather.forecastTime}")
        }
    }

    /** LGT > 0 이면 낙뢰 가능. */
    private fun hasLightning(lightningValue: String?): Boolean {
        return lightningValue?.toFloatOrNull()?.let { it > 0f } == true
    }

    /** 비·소나기·빗방울 계열 PTY. */
    private fun hasRain(precipitationCode: String?): Boolean {
        return precipitationCode in RAIN_PRECIPITATION_CODES
    }

    private val RAIN_PRECIPITATION_CODES = setOf("1", "4", "5")
}
