package com.yjp.onlyone.data.remote.dto

fun List<KmaItemDto>.observationValue(category: String): String? {
    return firstOrNull { it.category == category }?.observationValue
}

fun List<KmaItemDto>.forecastValue(
    category: String,
    forecastDate: String,
    forecastTime: String,
): String? {
    return firstOrNull {
        it.category == category &&
            it.forecastDate == forecastDate &&
            it.forecastTime == forecastTime
    }?.forecastValue
}

fun List<KmaItemDto>.dailyTemperature(category: String, forecastDate: String): String? {
    return firstOrNull {
        it.category == category && it.forecastDate == forecastDate
    }?.forecastValue
}

fun List<KmaItemDto>.resolveDailyMinMax(forecastDate: String): Pair<String?, String?> {
    return dailyTemperature("TMN", forecastDate) to dailyTemperature("TMX", forecastDate)
}

fun List<KmaItemDto>.forecastValuesForSlot(
    forecastDate: String,
    forecastTime: String,
): Map<String, String> {
    return forecastValuesForSlotOrNearest(forecastDate, forecastTime, allowNearest = false)
}

/** 정확한 슬롯이 없으면 이후 가장 가까운 예보 슬롯을 사용한다. */
fun List<KmaItemDto>.forecastValuesForSlotOrNearest(
    forecastDate: String,
    forecastTime: String,
    allowNearest: Boolean = true,
): Map<String, String> {
    val exact = filter {
        it.forecastDate == forecastDate && it.forecastTime == forecastTime
    }
    if (exact.isNotEmpty()) {
        return exact.toCategoryValueMap()
    }
    if (!allowNearest) return emptyMap()

    val targetKey = forecastDate + forecastTime
    return filter { !it.forecastDate.isNullOrBlank() && !it.forecastTime.isNullOrBlank() }
        .groupBy { it.forecastDate.orEmpty() + it.forecastTime.orEmpty() }
        .filterKeys { it >= targetKey }
        .minByOrNull { it.key }
        ?.value
        ?.toCategoryValueMap()
        ?: emptyMap()
}

private fun List<KmaItemDto>.toCategoryValueMap(): Map<String, String> {
    return mapNotNull { item ->
        item.category?.let { category ->
            category to item.forecastValue.orEmpty()
        }
    }.toMap()
}
