package com.aasati.hiltdemoapp.usecase

import com.aasati.hiltdemoapp.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * BaseUseCase
 *
 * Base use case to define input and output as wild cards and invoke operator for execution
 * @param Input - variable to use case
 * @return Output
 */
abstract class BaseUseCase<Input, Output : Any> {

    abstract suspend fun createSuspend(data: Input): Resource<Output>

    suspend operator fun invoke(withData: Input) = withContext(Dispatchers.IO) {
        createSuspend(withData)
    }
}
