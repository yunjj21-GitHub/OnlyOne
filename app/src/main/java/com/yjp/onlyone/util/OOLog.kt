package com.yjp.onlyone.util

import android.util.Log
import com.yjp.onlyone.BuildConfig

object OOLog {

    private const val DEFAULT_TAG = "OnlyOne"

    fun v(message: String) = v(DEFAULT_TAG, message)

    fun v(tag: String, message: String) {
        if (isDebugLogEnabled) Log.v(tag, message)
    }

    fun d(message: String) = d(DEFAULT_TAG, message)

    fun d(tag: String, message: String) {
        if (isDebugLogEnabled) Log.d(tag, message)
    }

    fun i(message: String) = i(DEFAULT_TAG, message)

    fun i(tag: String, message: String) {
        if (isDebugLogEnabled) Log.i(tag, message)
    }

    fun w(message: String) = w(DEFAULT_TAG, message)

    fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    fun w(message: String, throwable: Throwable) = w(DEFAULT_TAG, message, throwable)

    fun w(tag: String, message: String, throwable: Throwable) {
        Log.w(tag, message, throwable)
    }

    fun e(message: String) = e(DEFAULT_TAG, message)

    fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun e(message: String, throwable: Throwable) = e(DEFAULT_TAG, message, throwable)

    fun e(tag: String, message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }

    private val isDebugLogEnabled: Boolean
        get() = BuildConfig.DEBUG
}
