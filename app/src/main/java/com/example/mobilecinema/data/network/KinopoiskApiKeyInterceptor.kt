package com.example.mobilecinema.data.network

import android.util.Log
import com.example.mobilecinema.R
import com.example.mobilecinema.di.MainContext
import okhttp3.Interceptor
import okhttp3.Response

class KinopoiskApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithApiKey = originalRequest.newBuilder()
            .addHeader("X-API-KEY", "dc768ecc-f877-4755-98dd-5f95dd30e835")
            .build()

        Log.d("KinopoiskInterceptor", "Добавлен заголовок X-API-KEY")
        return chain.proceed(requestWithApiKey)
    }
}