package com.iapptm.coreapp.data.services

import android.content.SharedPreferences
import android.util.Log

class PrefsUtils constructor(private val prefs: SharedPreferences) {
    companion object {
        private const val PREFS_AUTH_ACCESSES_TOKEN = "AUTH_ACCESSES_TOKEN"
        private const val PREFS_AUTH_REFRESH_TOKEN = "AUTH_REFRESH_TOKEN"
    }

    fun getAccessesToken(): String? {
        return prefs.getString(PREFS_AUTH_ACCESSES_TOKEN, null)
    }

    fun setAccessesToken(accessToken: String) {
        prefs.edit().putString(PREFS_AUTH_ACCESSES_TOKEN, accessToken).apply()
    }

    fun getRefreshToken(): String? {
        return prefs.getString(PREFS_AUTH_REFRESH_TOKEN, null)
    }

    fun setRefreshToken(refreshToken: String) {
        prefs.edit().putString(PREFS_AUTH_REFRESH_TOKEN, refreshToken).apply()
    }

}
