package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.model.review.ReviewModifyModel
import kotlinx.coroutines.flow.Flow

interface ReviewRemoteDataSource {
    fun addReview(reviewModifyModel: ReviewModifyModel, movieId: String)

    fun editReview(moveId: String, reviewId: String, reviewModifyModel: ReviewModifyModel)

    fun deleteReview(moveId: String, reviewId: String)

}