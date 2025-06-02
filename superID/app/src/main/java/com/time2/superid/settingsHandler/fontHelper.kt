package com.time2.superid.settingsHandler

import android.content.Context

object FontPreferenceHelper {
    private const val PREF_NAME = "superid_prefs"
    private const val KEY_LARGE_FONT = "large_font_enabled"

    fun isLargeFont(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LARGE_FONT, false)
    }

    fun setLargeFont(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LARGE_FONT, enabled).apply()
    }
}
