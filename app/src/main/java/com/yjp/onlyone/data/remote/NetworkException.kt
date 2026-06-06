package com.yjp.onlyone.data.remote

import java.io.IOException

/** HTTP 상태 코드 오류. */
class HttpStatusException(
    val statusCode: Int,
    val url: String,
    val errorBody: String?,
    message: String = "HTTP $statusCode: $url",
) : IOException(message)

/** 응답 본문이 비어 있거나 파싱할 수 없는 경우. */
class EmptyResponseException(
    val url: String,
    message: String = "Empty response body: $url",
) : IOException(message)

/** 기상청 API 비즈니스 오류(resultCode != 00). */
class KmaApiException(
    val resultCode: String,
    val resultMessage: String,
    val url: String,
) : IOException("KMA API error [$resultCode] $resultMessage ($url)")
