package com.yjp.onlyone.data.remote.interceptor

import android.util.Log
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

/** 네트워크 요청/응답 로그 포맷 및 출력. */
internal object NetworkLogFormatter {

    private const val TAG = "OOnet"
    private const val MAX_LOG_LENGTH = 4000
    private const val SEPARATOR = "────────────────────────────────────────"
    private const val JSON_INDENT = 2

    fun logRequest(request: Request) {
        logMultiline(Log.INFO, formatRequest(request))
    }

    fun logResponse(response: Response, elapsedMs: Long, body: String) {
        logMultiline(Log.INFO, formatResponse(response, elapsedMs, body))
    }

    fun logHttpError(response: Response, body: String) {
        logMultiline(Log.ERROR, formatHttpError(response, body))
    }

    fun logError(message: String) {
        Log.e(TAG, message)
    }

    fun logError(message: String, throwable: Throwable) {
        Log.e(TAG, message, throwable)
    }

    private fun formatRequest(request: Request): String = buildString {
        appendLine(SEPARATOR)
        appendLine("--> ${request.method} ${request.url}")
        appendLine("Headers:")
        if (request.headers.size == 0) {
            appendLine("  (none)")
        } else {
            request.headers.forEach { (name, value) ->
                appendLine("  $name: $value")
            }
        }
        val requestBody = request.body
        if (requestBody != null) {
            appendLine("Body:")
            appendLine("  Content-Type: ${requestBody.contentType()}")
            appendLine("  Content-Length: ${requestBody.contentLength()}")
        }
    }

    private fun formatResponse(response: Response, elapsedMs: Long, body: String): String = buildString {
        val endpoint = response.request.url.pathSegments.lastOrNull() ?: "unknown"
        appendLine(SEPARATOR)
        appendLine("<-- ${response.code} $endpoint (${elapsedMs}ms)")
        appendLine("URL: ${response.request.url}")
        appendLine("Headers:")
        if (response.headers.size == 0) {
            appendLine("  (none)")
        } else {
            response.headers.forEach { (name, value) ->
                appendLine("  $name: $value")
            }
        }
        appendLine("Body:")
        append(prettyJson(body))
    }

    private fun formatHttpError(response: Response, body: String): String = buildString {
        val endpoint = response.request.url.pathSegments.lastOrNull() ?: "unknown"
        appendLine(SEPARATOR)
        appendLine("HTTP ERROR ${response.code} $endpoint")
        appendLine("URL: ${response.request.url}")
        appendLine("Headers:")
        response.headers.forEach { (name, value) ->
            appendLine("  $name: $value")
        }
        appendLine("Body:")
        append(prettyJson(body))
    }

    private fun prettyJson(body: String): String {
        if (body.isBlank()) return "(empty)"
        val trimmed = body.trim()
        return try {
            when {
                trimmed.startsWith("{") -> JSONObject(trimmed).toString(JSON_INDENT)
                trimmed.startsWith("[") -> JSONArray(trimmed).toString(JSON_INDENT)
                else -> body
            }
        } catch (_: Exception) {
            body
        }
    }

    private fun logMultiline(priority: Int, message: String) {
        message.lineSequence().forEach { line ->
            logChunked(priority, line)
        }
    }

    private fun logChunked(priority: Int, message: String) {
        var start = 0
        while (start < message.length) {
            val end = (start + MAX_LOG_LENGTH).coerceAtMost(message.length)
            Log.println(priority, TAG, message.substring(start, end))
            start = end
        }
    }
}
