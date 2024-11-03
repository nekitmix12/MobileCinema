package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.remote.data_source.ReviewRemoteDataSourceImpl
import com.example.mobilecinema.data.model.review.ReviewModifyModel
import com.example.mobilecinema.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReviewRepositoryImpl(
    private val reviewRemoteDataSourceImpl: ReviewRemoteDataSourceImpl,
) : ReviewRepository {
    override fun addReview(
        reviewModifyModel: ReviewModifyModel,
        movieId: String,
        id: String
    ): Flow<String> {
        reviewRemoteDataSourceImpl.addReview(reviewModifyModel, movieId)
        return flow {  }
    }

    override fun editReview(
        moveId: String,
        reviewId: String,
        reviewModifyModel: ReviewModifyModel
    ) {
        reviewRemoteDataSourceImpl.editReview(moveId, reviewId, reviewModifyModel)
    }

    override fun deleteReview(moveId: String, reviewId: String) {
        reviewRemoteDataSourceImpl.deleteReview(moveId,reviewId)
    }

}