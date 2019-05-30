package com.shenoto.domain.services

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData

class PrefsUtils constructor(private val prefs: SharedPreferences) {

    val isLoginLiveData = MutableLiveData<Boolean>().apply {
        postValue(prefs.getString(PREFS_AUTH_ACCESSES_TOKEN, null) != null)
    }

    companion object {
        private const val PREFS_AUTH_ACCESSES_TOKEN = "AUTH_ACCESSES_TOKEN"
        private const val PREFS_AUTH_REFRESH_TOKEN = "AUTH_REFRESH_TOKEN"
    }

    fun getAccessesToken(): String? {
        return prefs.getString(PREFS_AUTH_ACCESSES_TOKEN, null)
    }

    fun setAccessesToken(accessToken: String) {
        isLoginLiveData.postValue(true)
        prefs.edit().putString(PREFS_AUTH_ACCESSES_TOKEN, accessToken).apply()
    }

    fun getRefreshToken(): String? {
        return prefs.getString(PREFS_AUTH_REFRESH_TOKEN, null)
    }

    fun logout() {
        prefs.edit().clear().apply()
        isLoginLiveData.postValue(false)
    }


    fun setRefreshToken(refreshToken: String) {
        prefs.edit().putString(PREFS_AUTH_REFRESH_TOKEN, refreshToken).apply()
    }


}
