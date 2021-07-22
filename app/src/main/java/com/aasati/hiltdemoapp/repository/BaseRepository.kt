package com.aasati.hiltdemoapp.repository

import com.aasati.hiltdemoapp.common.DataError
import com.aasati.hiltdemoapp.common.Resource
import com.aasati.hiltdemoapp.common.ResponseWrapper
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

/**
 * BaseRepository
 *
 * Base repository to execute network requests and error handling
 */

open class BaseRepository {

    suspend fun <T : Any> execute(call: suspend () -> T) = try {
        ResponseWrapper.Success(call())
    } catch (ex: Throwable) {
        wrapException<T>(ex)
    }

    private fun <T : Any> wrapException(ex: Throwable) = when (ex) {
        is HttpException -> {
            when (ex.code()) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    ResponseWrapper.Error(
                        DataError.AuthorizationError(
                            ex.message(),
                            ex.code(),
                            ex
                        )
                    )
                }
                HttpURLConnection.HTTP_CONFLICT -> {
                    ResponseWrapper.Error(
                        DataError.ConflictError(
                            ex.message(),
                            ex.code(),
                            ex
                        )
                    )
                }
                else -> {
                    ex.response()?.errorBody()?.let {
                        ResponseWrapper.Error(DataError.ApiError(it.string(), ex.code(), ex))
                    } ?: ResponseWrapper.Error(DataError.ApiError(ex.message(), ex.code(), ex))
                }
            }
        }
        is SocketTimeoutException -> {
            ResponseWrapper.Error(DataError.TimeoutError(ex))
        }
        is IOException -> {
            ResponseWrapper.Error(DataError.NetworkError(ex))
        }
        is CancellationException -> {
            ResponseWrapper.Error(DataError.CancellationError(ex))
        }
        else -> {
            ResponseWrapper.Error(DataError.UnknownError(ex))
        }
    }
}
