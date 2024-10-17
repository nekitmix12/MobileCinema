package com.example.mobilecinema.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import javax.inject.Inject

class TokenStorageImpl (private val sharedPreferences: SharedPreferences):TokenStorage {

    override fun getToken(): String? {
        return sharedPreferences.getString("authToken", null)
    }


    override fun save(token: String) {
        sharedPreferences.edit().putString("authToken",token).apply()
    }

    override fun clearToken() {
        sharedPreferences.edit().remove("authToken").apply()
    }
}