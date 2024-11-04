package com.example.mobilecinema.domain.use_case.review_use_case

import com.example.mobilecinema.data.model.review.ReviewModifyModel
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddReviewUseCase(
    private val reviewRepository: ReviewRepository,
    private val configuration: Configuration,
) : UseCase<AddReviewUseCase.Request, AddReviewUseCase.Response>(configuration) {

    data class Response(val response: Unit) : UseCase.Response
    data class Request(
        val movieId: String,
        val id: String,
        val reviewModifyModel: ReviewModifyModel,
    ) : UseCase.Request

    override fun process(request: Request): Flow<Response> {
        return flow {
            emit(
                Response(
                    reviewRepository.addReview(
                        request.reviewModifyModel,
                        request.movieId,
                        request.id
                    )
                )
            )
        }
    }
}