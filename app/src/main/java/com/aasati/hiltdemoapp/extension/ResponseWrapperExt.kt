package com.aasati.hiltdemoapp.extension

import com.aasati.hiltdemoapp.common.DataError
import com.aasati.hiltdemoapp.common.Resource
import com.aasati.hiltdemoapp.common.ResponseWrapper
import com.aasati.hiltdemoapp.common.ResponseWrapper.Error
import com.aasati.hiltdemoapp.domain.mapper.toError

/**
 * zip
 *
 * Zip multiple ResponseWrappers into a pair of values.
 * An error in one of the two items is propagated to consumer.
 * ```
 */
fun <T : Any, U : Any> ResponseWrapper<T>.zip(other: ResponseWrapper<U>): ResponseWrapper<Pair<T, U>> {
    return when (this) {
        is ResponseWrapper.Success ->
            when (other) {
                is ResponseWrapper.Success -> ResponseWrapper.Success(Pair(this.data, other.data))
                is Error<*, *> -> other
            }
        is Error<*, *> -> this
    }
}

/**
 * map
 *
 * Transform the value contained within this response wrapper.
 * Error values are not transformed.
 */
inline fun <T : Any, U : Any> ResponseWrapper<T>.map(f: (T) -> U): ResponseWrapper<U> =
    when (this) {
        is ResponseWrapper.Success -> ResponseWrapper.Success(f(this.data))
        is Error<*, *> -> this
    }

/**
 * flatMap
 *
 * Transforms the value contained within this response wrapper and unwraps the result.
 * Error values are not transformed.
 *
 * When multiple `flatMap`s are used in series:
 *   - computation stops on the first error.
 *   - success values are propagated.
 *
 * when (chain) {
 *     is Success -> { /* Handle Success */ }
 *     is Error<*,*> -> { /* Handle Error */ }
 * }
 * ```
 */
inline fun <T : Any, U : Any> ResponseWrapper<T>.flatMap(f: (T) -> ResponseWrapper<U>): ResponseWrapper<U> =
    when (val result = map { f(it) }) {
        is ResponseWrapper.Success -> result.data
        is Error<*, *> -> result
    }

/**
 * mapNotNullOrError
 *
 * Apply a transformation function that returns a nullable value.
 *
 * If a null value is encountered, the provided error is returned.
 */
inline fun <T : Any, U : Any> ResponseWrapper<T>.mapNotNullOrError(
    error: DataError<*, *>,
    f: (T) -> U?
): ResponseWrapper<U> =
    when (this) {
        is ResponseWrapper.Success ->
            f(this.data)?.let { ResponseWrapper.Success(it) }
                ?: Error(
                    error
                )
        is Error<*, *> -> this
    }

/**
 * filter
 *
 * Returns a `ValidationError` error if the predicate is not successful.
 * The function has no effect on errors.
 *
 */
inline fun <T : Any> ResponseWrapper<T>.filter(predicate: (T) -> Boolean): ResponseWrapper<T> =
    when {
        this is ResponseWrapper.Success && predicate(this.data) -> this
        this is ResponseWrapper.Success -> Error(
            DataError.ValidationError(
                "Predicate not met for filter.",
                -1
            )
        )
        else -> this
    }

/**
 * tap
 *
 * Runs a function for side-effects on a success value. Has no effect on the value.
 * The function does not run on errors.
 *
 * when (chain) {
 *     is Success -> { /* Handle Success */ }
 *     is Error<*,*> -> { /* Handle Error */ }
 * }
 * ```
 */
inline fun <T : Any> ResponseWrapper<T>.tapSuccess(f: (T) -> Unit): ResponseWrapper<T> =
    when (this) {
        is ResponseWrapper.Success -> {
            f(this.data)
            this
        }
        is Error<*, *> -> {
            this
        }
    }

/**
 * catch
 *
 * Executes another action if the current ResponseWrapper is an error. This works similar to `flatMap`, but
 * only runs on Errors.
 */
inline fun <T : Any> ResponseWrapper<T>.catch(f: (DataError<*, *>) -> ResponseWrapper<T>) =
    when (this) {
        is ResponseWrapper.Success -> {
            this
        }
        is Error<*, *> -> {
            f(this.error)
        }
    }

/**
 * sequence
 *
 * Transforms a list of `ResponseWrapper`s to a list wrapped in a `ResponseWrapper`.
 * If any of the items are an `Error` the error is propagated as the result.
 * If all items are success, then the result is a list of the success values.
 */
fun <T : Any> List<ResponseWrapper<T>>.sequence(): ResponseWrapper<List<T>> {
    if (this.isEmpty()) {
        return ResponseWrapper.Success(emptyList())
    }

    val accumulator = mutableListOf<T>()
    for (item in this) {
        when (item) {
            is ResponseWrapper.Success -> accumulator.add(item.data)
            is Error<*, *> -> {
                return item
            }
        }
    }
    return ResponseWrapper.Success(accumulator)
}

/**
 * tapError
 *
 * Runs a function for side-effects on a error value. Has no effect on the value.This works similar to tap, but only runs on Errors.
 *
 */
inline fun <T : Any> ResponseWrapper<T>.tapError(f: (DataError<Any, Any>) -> Unit): ResponseWrapper<T> =
    when (this) {
        is ResponseWrapper.Success -> {
            this
        }
        is Error<*, *> -> {
            f(this.error)
            this
        }
    }

/**
 * VerifySuccessOrElse
 *
 * Verify the successful API response for given check
 *
 * If a check fails, the provided error is returned.
 */
inline fun <T : Any> ResponseWrapper<T>.verifySuccessOrElse(
    error: DataError<*, *>,
    f: (T) -> Boolean
): ResponseWrapper<T> =
    when (this) {
        is ResponseWrapper.Success ->
            if (f(this.data)) this else Error(error)
        is Error<*, *> -> this
    }

/**
 * toResource
 *
 * Transforms ResponseWrapper into a Resource to pass to the app
 */
fun <T : Any> ResponseWrapper<T>.toResource(): Resource<T> {
    return when (this) {
        is ResponseWrapper.Success -> Resource.Success(this.data)
        is Error<*, *> -> Resource.Error(this.error.toError())
    }
}

/**
 * get
 *
 * Get the value if the ReponseWrapper was a success. Null otherwise.
 */
fun <T : Any> ResponseWrapper<T>.get(): T? {
    return when (this) {
        is ResponseWrapper.Success -> this.data
        is Error<*, *> -> null
    }
}

/**
 * asSuccess()
 *
 * Returns the receiver object in a ResponseWrapper.Success
 */
fun <T : Any> T.asSuccess() = ResponseWrapper.Success(this)

/**
 * asError()
 *
 * Return the receiver object as a ResponseWrapper.Error
 */
fun <T : Any, U : Any, E : DataError<T, U>> E.asError() = Error(this)
