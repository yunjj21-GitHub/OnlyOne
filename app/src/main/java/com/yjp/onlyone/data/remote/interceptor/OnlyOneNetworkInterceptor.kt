package com.yjp.onlyone.data.remote.interceptor

import com.yjp.onlyone.data.remote.EmptyResponseException
import com.yjp.onlyone.data.remote.HttpStatusException
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import java.util.concurrent.TimeUnit

/**
 * 공통 네트워크 인터셉터.
 * - 요청/응답 상세 로깅 ([NetworkLogFormatter])
 * - HTTP 오류 상태 코드 처리
 * - 빈 응답 본문 검증
 */
class OnlyOneNetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startedAtNs = System.nanoTime()
        val endpoint = request.url.pathSegments.lastOrNull() ?: "unknown"

        NetworkLogFormatter.logRequest(request)

        val response = try {
            chain.proceed(request)
        } catch (exception: IOException) {
            NetworkLogFormatter.logError("I/O failed $endpoint", exception)
            throw exception
        }

        val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAtNs)
        val bodyPreview = response.peekBody(MAX_LOG_BODY_BYTES).string()

        if (!response.isSuccessful) {
            NetworkLogFormatter.logHttpError(response, bodyPreview)
            throw HttpStatusException(
                statusCode = response.code,
                url = request.url.toString(),
                errorBody = bodyPreview.takeIf { it.isNotBlank() },
            )
        }

        NetworkLogFormatter.logResponse(response, elapsedMs, bodyPreview)

        if (response.body == null) {
            NetworkLogFormatter.logError("Empty response body: $endpoint")
            response.close()
            throw EmptyResponseException(request.url.toString())
        }

        return response
    }

    private companion object {
        const val MAX_LOG_BODY_BYTES = 256 * 1024L
    }
}
