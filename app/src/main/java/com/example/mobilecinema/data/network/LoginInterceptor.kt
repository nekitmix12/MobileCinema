package com.example.mobilecinema.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoginInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("RequestLogger", "URL: ${request.url}")
        Log.d("RequestLogger", "Headers: ${request.headers}")
        Log.d("RequestLogger", "Method: ${request.method}")

        request.body?.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            Log.d("RequestLogger", "Body: ${buffer.readUtf8()}")
        }
        val startTime = System.nanoTime()
        Log.d("log_interceptor",("Sending request ${request.url} on ${chain.connection()}\n${request.headers}"))

        val response = chain.proceed(request)

        val endTime = System.nanoTime()
        Log.d("log_interceptor",("Received response for ${response.request.url} in ${(endTime - startTime) / 1e6} ms\n$response"))
        Log.d("log_interceptor",("Code: ${response.code} ${response.message}"))
        return response
    }
}