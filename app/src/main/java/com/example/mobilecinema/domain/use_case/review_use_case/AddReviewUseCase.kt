package com.example.mobilecinema.domain.use_case.review_use_case

import com.example.mobilecinema.data.model.review.ReviewModifyModel
import com.example.mobilecinema.data.repository.ReviewRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.ReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddReviewUseCase(
    private val reviewRepository: ReviewRepository = ReviewRepositoryImpl(),
    private val configuration: Configuration = Configuration(Dispatchers.IO),
) : UseCase<AddReviewUseCase.Request, AddReviewUseCase.Response>(configuration) {

    data class Response(val response: Unit) : UseCase.Response
    data class Request(
        val movieId: String,
        val reviewModifyModel: ReviewModifyModel,
    ) : UseCase.Request

    override fun process(request: Request): Flow<Response> = reviewRepository.addReview(
        request.reviewModifyModel,
        request.movieId,
    ).map { Response(it) }


}