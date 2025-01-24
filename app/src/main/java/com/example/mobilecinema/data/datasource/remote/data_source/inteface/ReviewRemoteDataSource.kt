package com.example.mobilecinema.data.datasource.remote.data_source.inteface

import com.example.mobilecinema.data.model.review.ReviewModifyModel
import kotlinx.coroutines.flow.Flow

interface ReviewRemoteDataSource {
    fun addReview(reviewModifyModel: ReviewModifyModel, movieId: String):Flow<Unit>

    fun editReview(moveId: String, reviewId: String, reviewModifyModel: ReviewModifyModel):Flow<Unit>

    fun deleteReview(moveId: String, reviewId: String):Flow<Unit>

}