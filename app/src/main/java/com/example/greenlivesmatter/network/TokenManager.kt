package com.example.greenlivesmatter.network

import android.content.Context

object TokenManager {
    private const val PREFERENCE_NAME = "example_preferences"
    private const val TOKEN_KEY = "access_token"

    fun saveToken(context: Context, token: String?) {
        if (token != null) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
        } else {
            // Выводите сообщение об ошибке или предупреждение, чтобы отслеживать проблему
        }
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, null)
    }
}