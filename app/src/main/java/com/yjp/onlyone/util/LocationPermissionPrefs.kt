package com.yjp.onlyone.util

import android.content.Context

/** 앱 최초 실행 시 위치 권한 안내 여부. */
object LocationPermissionPrefs {

    private const val PREFS_NAME = "onlyone_location_prefs"
    private const val KEY_FIRST_LAUNCH_PROMPT_SHOWN = "first_launch_location_prompt_shown"

    fun shouldShowFirstLaunchPrompt(context: Context): Boolean {
        return !context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_FIRST_LAUNCH_PROMPT_SHOWN, false)
    }

    fun markFirstLaunchPromptShown(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_FIRST_LAUNCH_PROMPT_SHOWN, true)
            .apply()
    }
}
