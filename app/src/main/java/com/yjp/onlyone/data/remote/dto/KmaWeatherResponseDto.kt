package com.yjp.onlyone.data.remote.dto

import com.google.gson.annotations.SerializedName

data class KmaWeatherResponseDto(
    val response: KmaResponseDto? = null,
)

data class KmaResponseDto(
    val header: KmaHeaderDto? = null,
    val body: KmaBodyDto? = null,
)

data class KmaHeaderDto(
    val resultCode: String? = null,
    @SerializedName("resultMsg") val resultMessage: String? = null,
)

data class KmaBodyDto(
    val dataType: String? = null,
    val items: KmaItemsDto? = null,
    val pageNo: Int? = null,
    val numOfRows: Int? = null,
    val totalCount: Int? = null,
)

/** [com.yjp.onlyone.data.remote.gson.KmaItemsDtoDeserializer]로 파싱한다. */
data class KmaItemsDto(
    val item: List<KmaItemDto> = emptyList(),
)

data class KmaItemDto(
    val baseDate: String? = null,
    val baseTime: String? = null,
    val category: String? = null,
    @SerializedName("fcstDate") val forecastDate: String? = null,
    @SerializedName("fcstTime") val forecastTime: String? = null,
    @SerializedName("fcstValue") val forecastValue: String? = null,
    @SerializedName("obsrValue") val observationValue: String? = null,
    val nx: Int? = null,
    val ny: Int? = null,
)
