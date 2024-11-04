package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.review.ReviewModifyModel
import kotlinx.coroutines.flow.Flow

interface ReviewRepository{
    suspend fun addReview(reviewModifyModel: ReviewModifyModel, movieId:String,id: String)

    suspend fun editReview(moveId:String,reviewId:String,reviewModifyModel: ReviewModifyModel)

    suspend fun deleteReview(moveId:String,reviewId:String)
}