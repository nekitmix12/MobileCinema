package com.example.mobilecinema.domain

sealed class UseCaseException(override val cause: Throwable?) :
    Throwable(cause) {
    class UserException(cause: Throwable) :
        UseCaseException(cause)

    class UnknownException(cause: Throwable) :
        UseCaseException(cause)

    companion object {
        fun extractException(throwable: Throwable):
                UseCaseException {
            return if (throwable is UseCaseException)
                throwable else UnknownException(throwable)
        }
    }
}