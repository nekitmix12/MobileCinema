package com.example.mobilecinema.data.network

import android.content.Context
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceAuth
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceFavoriteMovies
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceMovie
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceReview
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceUser
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class NetworkModule {

    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loginInterceptor: LoginInterceptor = LoginInterceptor()): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loginInterceptor)
        .readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://react-midterm.kreosoft.space/")
        .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    fun provideAuthService(retrofit: Retrofit):
            ApiServiceAuth =
        retrofit.create(ApiServiceAuth::class.java)
    fun provideFavoriteMoviesService(retrofit: Retrofit):
            ApiServiceFavoriteMovies =
        retrofit.create(ApiServiceFavoriteMovies::class.java)
    fun provideMovieService(retrofit: Retrofit):
            ApiServiceMovie =
        retrofit.create(ApiServiceMovie::class.java)
    fun provideReviewService(retrofit: Retrofit):
            ApiServiceReview =
        retrofit.create(ApiServiceReview::class.java)
    fun provideUserService(retrofit: Retrofit):
            ApiServiceUser =
        retrofit.create(ApiServiceUser::class.java)

    fun provideTokenStorage(context: Context): TokenStorageDataSourceImpl =
        TokenStorageDataSourceImpl(context.getSharedPreferences(context.getString(R.string.preference_is_logged_in), Context.MODE_PRIVATE))
}