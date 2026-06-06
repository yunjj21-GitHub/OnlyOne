package com.yjp.onlyone.util

/** 기상청 SKY / PTY 코드를 화면 표시 문자열로 변환한다. */
object KmaWeatherCodeLabel {

    fun weather(skyCode: String?, precipitationCode: String?, shortTerm: Boolean = false): String {
        val precipitationLabel = precipitationLabel(precipitationCode, shortTerm)
        if (precipitationCode != null && precipitationCode != "0") {
            return precipitationLabel
        }
        return skyLabel(skyCode)
    }

    fun skyLabel(code: String?): String {
        return when (code) {
            "1" -> "맑음"
            "3" -> "구름많음"
            "4" -> "흐림"
            null, "" -> "-"
            else -> "하늘($code)"
        }
    }

    private fun precipitationLabel(code: String?, shortTerm: Boolean): String {
        if (code == null || code == "0") return "없음"
        return if (shortTerm) {
            when (code) {
                "1" -> "비"
                "2" -> "비/눈"
                "3" -> "눈"
                "5" -> "빗방울"
                "6" -> "빗방울눈날림"
                "7" -> "눈날림"
                else -> "강수($code)"
            }
        } else {
            when (code) {
                "1" -> "비"
                "2" -> "비/눈"
                "3" -> "눈"
                "4" -> "소나기"
                else -> "강수($code)"
            }
        }
    }
}
