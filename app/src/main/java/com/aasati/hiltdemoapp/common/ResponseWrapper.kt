package com.aasati.hiltdemoapp.common

/**
 * ResponseWrapper
 *
 * Wrapper for data/error
 */
sealed class ResponseWrapper<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResponseWrapper<T>()

    data class Error<T : Any, U : Any>(val error: DataError<T, U>) : ResponseWrapper<Nothing>()
}
