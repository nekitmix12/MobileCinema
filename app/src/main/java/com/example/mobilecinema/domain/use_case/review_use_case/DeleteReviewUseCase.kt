package com.example.mobilecinema.domain.use_case.review_use_case

import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteReviewUseCase(
    private val reviewRepository: ReviewRepository,
    private val configuration: UseCase.Configuration,
) : UseCase<DeleteReviewUseCase.Request, DeleteReviewUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        flow {
            emit(
                Response(
                    reviewRepository.deleteReview(request.reviewId, request.movieId)
                )
            )
        }

    data class Response(val answer: Unit) : UseCase.Response
    data class Request(
        val movieId: String,
        val reviewId: String,
    ) : UseCase.Request

}