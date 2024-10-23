package com.example.mobilecinema.data.datasource.local

import android.content.SharedPreferences
import com.example.mobilecinema.domain.repository.LocalStorageRepository

class TokenStorageDataSourceImpl (private val sharedPreferences: SharedPreferences): LocalStorageRepository {

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