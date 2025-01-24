package com.example.mobilecinema.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import com.example.mobilecinema.R
import com.example.mobilecinema.di.MainContext
import com.example.mobilecinema.domain.repository.LocalStorageRepository

class TokenStorageDataSourceImpl(
    private val sharedPreferences: SharedPreferences = MainContext.provideInstance()
        .provideContext()
        .getSharedPreferences(
            MainContext.provideInstance().provideContext()
                .getString(R.string.preference_is_logged_in), Context.MODE_PRIVATE
        ),
) : LocalStorageRepository {

    override fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }


    override fun save(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    override fun clearToken() {
        sharedPreferences.edit().remove("token").apply()
    }
}