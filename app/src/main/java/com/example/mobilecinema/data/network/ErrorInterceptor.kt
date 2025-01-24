package com.example.mobilecinema.data.network

import android.util.Log
import com.example.mobilecinema.data.ErrorsConverter
import com.example.mobilecinema.data.ErrorsConverterImpl
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class ErrorInterceptor(private val errorsConverter: ErrorsConverter = ErrorsConverterImpl()) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            Log.d("login error api","interceptor work")

            val modifiedBody =
                "{\"message\":\"${errorsConverter.invokeMethod(response.body, response.code)}\"}"
                    .toResponseBody(response.body?.contentType())
            return response.newBuilder()
                .body(modifiedBody)
                .build()
        }
        return response
    }
}