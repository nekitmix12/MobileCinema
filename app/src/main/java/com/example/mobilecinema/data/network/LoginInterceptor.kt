package com.example.mobilecinema.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoginInterceptor():Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val startTime = System.nanoTime()
        Log.d("log_interceptor",("Sending request ${request.url} on ${chain.connection()}\n${request.headers}"))

        val response = chain.proceed(request)

        val endTime = System.nanoTime()
        Log.d("log_interceptor",("Received response for ${response.request.url} in ${(endTime - startTime) / 1e6} ms\n$response"))

        return response
    }
}