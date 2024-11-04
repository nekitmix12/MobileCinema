package com.example.mobilecinema.domain.use_case.review_use_case

import com.example.mobilecinema.data.model.review.ReviewModifyModel
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EditReviewUseCase(
    private val reviewRepository: ReviewRepository,
    configuration: Configuration,
) : UseCase<EditReviewUseCase.Request, EditReviewUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> = flow {
        Response(
            reviewRepository.editReview(
                request.movieId, request.reviewId, request.moviesModifyModel
            )
        )
    }

    data class Request(
        val movieId: String,
        val reviewId: String,
        val moviesModifyModel: ReviewModifyModel,
    ) : UseCase.Request

    data class Response(val answer: Unit) : UseCase.Response

}