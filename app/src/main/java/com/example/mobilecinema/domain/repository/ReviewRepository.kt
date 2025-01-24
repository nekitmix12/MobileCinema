package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.review.ReviewModifyModel
import kotlinx.coroutines.flow.Flow

interface ReviewRepository{
    fun addReview(reviewModifyModel: ReviewModifyModel, movieId:String):Flow<Unit>

    fun editReview(moveId:String,reviewId:String,reviewModifyModel: ReviewModifyModel):Flow<Unit>

    fun deleteReview(moveId:String,reviewId:String):Flow<Unit>
}