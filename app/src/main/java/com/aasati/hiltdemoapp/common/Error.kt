package com.aasati.hiltdemoapp.common

import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

/**
 * Error
 *
 * Wrapper class for all types of errors
 */
sealed class Error<out T : Any, out U : Any> {

    /**
     * Api Failure Response Error
     */
    data class ApiError<U : Any>(val body: U, val code: Int) : Error<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : Error<Nothing, Nothing>()

    /**
     * Validation Error
     */
    data class ValidationError<U : Any>(val body: U, val code: Int) : Error<Nothing, U>()

    /**
     * Authorization Error
     */
    data class AuthorizationError<U : Any, T : Any>(val message: String, val code: Int) :
        Error<U, T>()

    /**
     * Conflict Error
     */
    data class ConflictError<U : Any, T : Any>(val message: String, val code: Int) :
        Error<U, T>()

    /**
     * API Time Out Error
     */
    data class TimeoutError(val error: SocketTimeoutException) : Error<Nothing, Nothing>()

    /**
     * Cancellation Error
     */
    data class CancellationError(val error: CancellationException) : Error<Nothing, Nothing>()

    /**
     * Unknown Error
     */
    data class UnknownError(val error: Throwable) : Error<Nothing, Nothing>()
}
