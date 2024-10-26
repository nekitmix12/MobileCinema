package com.example.mobilecinema.data.network

import android.util.Log
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (private val tokenStorage: TokenStorageDataSourceImpl):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenStorage.getToken()
        Log.d("Auth_interceptor",token.toString())
        return if (token != null) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }
}