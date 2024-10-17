package com.example.mobilecinema.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

//подсказка себе: any- для того чтоб нельзя было поставить null, ну и в конуструктор передаем выбор потока
//происходит тут следующее : мы получаем унаследованную функцию, мапим значени из потока входного класса в результат выходного используя выданный нам поток и если что то поломалось то выкидываем ошибку


// идея скомунизжена из учебника Clean Android Architecture Alexandru Dumbravan(учебник бомба, но со сложными механизмами :),в общем то вся архитектура приложения выстраивалась благодаря этой книге )
abstract class UseCase<I : Any, O : Any>(private val dispatcher: CoroutineDispatcher) {
    fun execute(input: I): Flow<Result<O>> =
        executeData(input)
            .map {
                Result.Success(it) as Result<O>
            }
            .flowOn(dispatcher)
            .catch {
                emit(
                    Result.Error(
                        UseCaseException.extractException(it)
                    )
                )
            }

    internal abstract fun executeData(input: I): Flow<O>

}