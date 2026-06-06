package com.yjp.onlyone.data.remote.dto

import com.yjp.onlyone.data.remote.KmaApiException

private const val KMA_SUCCESS_CODE = "00"

fun KmaWeatherResponseDto.requireSuccess(requestUrl: String): KmaWeatherResponseDto {
    val header = response?.header
    val resultCode = header?.resultCode ?: throw KmaApiException(
        resultCode = "UNKNOWN",
        resultMessage = "Missing response header",
        url = requestUrl,
    )

    if (resultCode != KMA_SUCCESS_CODE) {
        throw KmaApiException(
            resultCode = resultCode,
            resultMessage = header.resultMessage.orEmpty(),
            url = requestUrl,
        )
    }

    return this
}

fun KmaWeatherResponseDto.itemsOrEmpty(): List<KmaItemDto> {
    return response?.body?.items?.item.orEmpty()
}
