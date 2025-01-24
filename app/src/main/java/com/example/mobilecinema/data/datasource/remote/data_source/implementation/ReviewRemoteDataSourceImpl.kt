package com.example.mobilecinema.data.datasource.remote.data_source.implementation

import com.example.mobilecinema.data.ErrorsConverterImpl
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceReview
import com.example.mobilecinema.data.datasource.remote.data_source.inteface.ReviewRemoteDataSource
import com.example.mobilecinema.data.model.review.ReviewModifyModel
import com.example.mobilecinema.data.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ReviewRemoteDataSourceImpl(
    private val apiServiceReview: ApiServiceReview = NetworkModule().provideReviewService(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient()
        )
    ),
) : ReviewRemoteDataSource {
    override fun addReview(
        reviewModifyModel: ReviewModifyModel,
        movieId: String,
    ): Flow<Unit> = flow {
        emit(apiServiceReview.addReview(movieId, reviewModifyModel))
    }.map {
        if (it.body() != null)
            Unit
        else
            throw IllegalArgumentException(
                ErrorsConverterImpl().invokeMethod(
                    it.errorBody(),
                    it.code()
                )
            )
    }

    override fun editReview(
        moveId: String,
        reviewId: String,
        reviewModifyModel: ReviewModifyModel,
    ): Flow<Unit> = flow {
        emit(apiServiceReview.editReview(moveId, reviewId, reviewModifyModel))
    }.map {
        if (it.body() != null)
            Unit
        else
            throw IllegalArgumentException(
                ErrorsConverterImpl().invokeMethod(
                    it.errorBody(),
                    it.code()
                )
            )
    }

    override fun deleteReview(moveId: String, reviewId: String): Flow<Unit> = flow {
        emit(apiServiceReview.deleteReview(moveId, reviewId))
    }.map {
        if (it.body() != null)
            Unit
        else
            throw IllegalArgumentException(
                ErrorsConverterImpl().invokeMethod(
                    it.errorBody(),
                    it.code()
                )
            )
    }

}