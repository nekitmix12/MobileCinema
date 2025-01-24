package com.example.mobilecinema.domain.use_case.review_use_case

import com.example.mobilecinema.data.repository.ReviewRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.ReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeleteReviewUseCase(
    private val reviewRepository: ReviewRepository = ReviewRepositoryImpl(),
    private val configuration: UseCase.Configuration = Configuration(Dispatchers.IO),
) : UseCase<DeleteReviewUseCase.Request, DeleteReviewUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =

        reviewRepository.deleteReview(request.reviewId, request.movieId).map { Response(it) }


    data class Response(val answer: Unit) : UseCase.Response
    data class Request(
        val movieId: String,
        val reviewId: String,
    ) : UseCase.Request

}