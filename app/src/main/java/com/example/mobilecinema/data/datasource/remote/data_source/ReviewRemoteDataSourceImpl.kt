package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceReview
import com.example.mobilecinema.data.model.review.ReviewModifyModel
import kotlinx.coroutines.flow.Flow

class ReviewRemoteDataSourceImpl(
    private val apiServiceReview: ApiServiceReview
):ReviewRemoteDataSource{
    override suspend fun addReview(reviewModifyModel: ReviewModifyModel, movieId:String) {
        apiServiceReview.addReview(movieId,reviewModifyModel)
    }

    override suspend fun editReview(moveId:String, reviewId:String, reviewModifyModel: ReviewModifyModel){
        apiServiceReview.editReview(moveId,reviewId,reviewModifyModel)
    }

    override suspend fun deleteReview(moveId:String, reviewId:String){
        apiServiceReview.deleteReview(moveId,reviewId)
    }

}