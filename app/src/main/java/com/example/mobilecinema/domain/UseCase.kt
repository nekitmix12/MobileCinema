package com.example.mobilecinema.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

//происходит тут следующее : мы получаем

// идея скоммунизжена из учебника Clean Android Architecture Alexandru Dumbravan(учебник бомба, но со сложными механизмами :),в общем то вся архитектура приложения выстраивалась благодаря этой книге )
abstract class UseCase<I : UseCase.Request, O : UseCase.
Response>(private val configuration: Configuration) {
    fun execute(request: I) = process(request)
        .map {
            Result.Success(it) as Result<O>
        }
        .flowOn(configuration.dispatcher)
        .catch {
            emit(Result.Error(UseCaseException.
            extractException(it)))
        }
    internal abstract fun process(request: I): Flow<O>
    class Configuration(val dispatcher:
                        CoroutineDispatcher)
    interface Request
    interface Response
}