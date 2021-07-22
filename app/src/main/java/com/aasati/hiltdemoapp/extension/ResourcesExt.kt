package com.aasati.hiltdemoapp.extension

import com.aasati.hiltdemoapp.common.Error
import com.aasati.hiltdemoapp.common.Resource

/**
 * flatMap
 *
 * Utility method to chain sequential resource calls.
 */
inline fun <T : Any, U : Any> Resource<T>.flatMap(f: (T) -> Resource<U>) =
    when (val result = map(f)) {
        is Resource.Success -> result.data
        is Resource.Error<*, *> -> result
    }

/**
 * tap
 *
 * Utility method to chain sequential resource calls.
 */
inline fun <T : Any, U : Any> Resource<T>.tap(f: (T) -> U) = when (this) {
    is Resource.Success -> {
        f(this.data)
        this
    }
    is Resource.Error<*, *> -> {
        this
    }
}

/**
 * catch
 *
 * Utility method to map an error response, or recover from an error.
 * This is the equivalent of `flatMap` that only acts on Resource.Error<*, *>.
 *
 * @param block function to process the error. Returns another Resource.
 */
inline fun <T : Any> Resource<T>.catch(block: (Error<*, *>) -> Resource<T>): Resource<T> =
    when (this) {
        is Resource.Success -> this
        is Resource.Error<*, *> -> block(this.error)
    }

/**
 * map
 *
 * Utility method to apply a function to a Resource.Success. Errors remain unaffected.
 */
inline fun <T : Any, U : Any> Resource<T>.map(f: (T) -> U) = when (this) {
    is Resource.Success -> Resource.Success(f(this.data))
    is Resource.Error<*, *> -> this
}

/**
 * zip
 *
 * Zip two Resource.Success into a Pair. If one is an error, then the error is presented.
 */
fun <T : Any, U : Any> Resource<T>.zip(other: Resource<U>) = when (this) {
    is Resource.Success -> other.map { Pair(this.data, it) }
    is Resource.Error<*, *> -> this
}

/**
 * zip
 *
 * Zip three Resource.Success into a Triple. If one is an error, then the error is presented.
 */
fun <T : Any, U : Any, V : Any> Resource<T>.zip(other1: Resource<U>, other2: Resource<V>) =
    when (this) {
        is Resource.Success -> other1.flatMap { second ->
            other2.map { third ->
                Triple(
                    this.data,
                    second,
                    third
                )
            }
        }
        is Resource.Error<*, *> -> this
    }

/**
 * onError
 *
 * Function to run an action when the Result is Error. Action is not called if the response is a Success.
 */
inline fun <T : Any> Resource<T>.onError(action: (Error<*, *>) -> Unit): Resource<T> {
    if (this is Resource.Error<*, *> && this.error !is Error.CancellationError) {
        action(this.error)
    }
    return this
}

/**
 * onErrorOf
 *
 * Function to run an action when the Result is a specific type of Error. Action is not called if the response is a Success.
 */
inline fun <reified E : Error<*, *>> Resource<*>.onErrorOf(action: (E) -> Unit): Resource<*> {
    if (this is Resource.Error<*, *> && this.error is E) {
        action(this.error)
    }
    return this
}

/**
 * onSuccess
 *
 * Function to run an action when the Result is Success. Action is not called if the response is an Error.
 */
inline fun <T : Any> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) {
        action(this.data)
    }
    return this
}

/**
 * contains
 *
 * Determine if the Resource is a Success and contains a value matching the predicate
 */
fun <T : Any> Resource<T>.contains(predicate: (T) -> Boolean): Boolean =
    map { predicate(it) }
        .getOrElse(false)

/**
 * getOrElse
 *
 * Extract the value if it is a Resource.Success, or use the provided default value.
 */
fun <T : Any> Resource<T>.getOrElse(defaultValue: T): T {
    if (this is Resource.Success) {
        return this.data
    }
    return defaultValue
}

/**
 * orElse
 *
 * Provide an alternative value when the resource is an error.
 */
fun <T : Any> Resource<T>.orElse(defaultValue: T): Resource<T> {
    if (this is Resource.Success) {
        return this
    }
    return Resource.Success(defaultValue)
}

/**
 * get
 *
 * Extract the value if it is a Resource.Success, or return null.
 */
fun <T : Any> Resource<T>.get(): T? {
    if (this is Resource.Success) {
        return this.data
    }
    return null
}

/**
 * asResourceSuccess()
 *
 * Returns the receiver object in a Resource.Success
 */
fun <T : Any> T.asResourceSuccess() = Resource.Success(this)

/**
 * isError()
 *
 * true if the resource is of type error
 */
fun <T : Any> Resource<T>.isError() = this is Resource.Error<*, *>

/**
 * isSuccess()
 *
 * true if the resource is of type success
 */
fun <T : Any> Resource<T>.isSuccess() = this is Resource.Success<*>
