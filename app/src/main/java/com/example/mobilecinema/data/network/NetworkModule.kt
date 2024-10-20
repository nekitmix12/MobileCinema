package com.example.mobilecinema.data.network

import android.content.Context
import com.example.mobilecinema.data.TokenStorageImpl
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceAuth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class NetworkModule {

    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()


    @OptIn(ExperimentalSerializationApi::class)
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://react-midterm.kreosoft.space/api")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    fun provideUserService(retrofit: Retrofit):
            ApiServiceAuth =
        retrofit.create(ApiServiceAuth::class.java)

    fun provideTokenStorage(context: Context): TokenStorageImpl =
        TokenStorageImpl(context.getSharedPreferences("authPrefs", Context.MODE_PRIVATE))
}