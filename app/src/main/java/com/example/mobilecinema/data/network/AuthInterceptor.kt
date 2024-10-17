package com.example.mobilecinema.data.network

import com.example.mobilecinema.data.TokenStorage
import com.example.mobilecinema.domain.repository.UserRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor (private val tokenStorage: TokenStorage):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenStorage.getToken()

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