package com.aasati.hiltdemoapp.common

/**
 * Resource
 *
 * Wrapper class for data/errors passed
 */
sealed class Resource<out T : Any> {

    /**
     * Represents a successful retrieval.
     */
    data class Success<out T : Any>(val data: T) : Resource<T>()

    /**
     * Represents a failed retrieval.
     */
    data class Error<T : Any, U : Any>(val error: com.aasati.hiltdemoapp.common.Error<T, U>) :
        Resource<Nothing>()
}
