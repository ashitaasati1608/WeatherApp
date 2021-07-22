package com.aasati.hiltdemoapp.domain.mapper

import com.aasati.hiltdemoapp.common.DataError
import com.aasati.hiltdemoapp.common.Error

/**
 * DataError.toError
 *
 * Mapping DataError -> Error
 */
fun DataError<*, *>.toError(): Error<*, *> {
    when (this) {
        is DataError.ApiError -> {
            return Error.ApiError(body, code)
        }

        is DataError.NetworkError -> {
            return Error.NetworkError(error)
        }
        is DataError.ValidationError -> {
            return Error.ValidationError(body, code)
        }
        is DataError.AuthorizationError -> {
            return Error.AuthorizationError<String, Int>(message, code)
        }
        is DataError.ConflictError -> {
            return Error.ConflictError<String, Int>(message, code)
        }
        is DataError.TimeoutError -> {
            return Error.TimeoutError(error)
        }
        is DataError.CancellationError -> {
            return Error.CancellationError(error)
        }
        is DataError.UnknownError -> {
            return Error.UnknownError(error)
        }
    }
}
