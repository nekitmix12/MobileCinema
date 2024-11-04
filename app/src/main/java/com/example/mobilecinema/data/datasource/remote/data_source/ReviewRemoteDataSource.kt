package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.model.review.ReviewModifyModel
import kotlinx.coroutines.flow.Flow

interface ReviewRemoteDataSource {
    suspend fun addReview(reviewModifyModel: ReviewModifyModel, movieId: String)

    suspend fun editReview(moveId: String, reviewId: String, reviewModifyModel: ReviewModifyModel)

    suspend fun deleteReview(moveId: String, reviewId: String)

}