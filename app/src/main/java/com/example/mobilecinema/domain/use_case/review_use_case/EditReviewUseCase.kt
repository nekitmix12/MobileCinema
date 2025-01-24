package com.example.mobilecinema.domain.use_case.review_use_case

import com.example.mobilecinema.data.model.review.ReviewModifyModel
import com.example.mobilecinema.data.repository.ReviewRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.ReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EditReviewUseCase(
    private val reviewRepository: ReviewRepository = ReviewRepositoryImpl(),
    configuration: Configuration = Configuration(Dispatchers.IO),
) : UseCase<EditReviewUseCase.Request, EditReviewUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        reviewRepository.editReview(
            request.movieId, request.reviewId, request.moviesModifyModel
        ).map { Response(it) }


    data class Request(
        val movieId: String,
        val reviewId: String,
        val moviesModifyModel: ReviewModifyModel,
    ) : UseCase.Request

    data class Response(val answer: Unit) : UseCase.Response

}