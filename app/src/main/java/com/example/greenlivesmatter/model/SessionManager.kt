package com.example.greenlivesmatter.model

import android.content.Context
import android.content.SharedPreferences

class SessionManager (context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString("AUTH_TOKEN", token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString("AUTH_TOKEN", null)
    }
}