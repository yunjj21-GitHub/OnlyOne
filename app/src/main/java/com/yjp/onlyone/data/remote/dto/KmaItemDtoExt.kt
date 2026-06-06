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
    return filter {
        it.forecastDate == forecastDate && it.forecastTime == forecastTime
    }.mapNotNull { item ->
        item.category?.let { category ->
            category to (item.forecastValue.orEmpty())
        }
    }.toMap()
}
