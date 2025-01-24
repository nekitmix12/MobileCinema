package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.remote.data_source.implementation.ReviewRemoteDataSourceImpl
import com.example.mobilecinema.data.model.review.ReviewModifyModel
import com.example.mobilecinema.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow

class ReviewRepositoryImpl(
    private val reviewRemoteDataSourceImpl: ReviewRemoteDataSourceImpl = ReviewRemoteDataSourceImpl(),
) : ReviewRepository {
    override fun addReview(
        reviewModifyModel: ReviewModifyModel,
        movieId: String,
    ): Flow<Unit> = reviewRemoteDataSourceImpl.addReview(reviewModifyModel, movieId)


    override fun editReview(
        moveId: String,
        reviewId: String,
        reviewModifyModel: ReviewModifyModel,
    ): Flow<Unit> = reviewRemoteDataSourceImpl.editReview(moveId, reviewId, reviewModifyModel)

    override fun deleteReview(moveId: String, reviewId: String): Flow<Unit> =
        reviewRemoteDataSourceImpl.deleteReview(moveId, reviewId)


}