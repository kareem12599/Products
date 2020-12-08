package com.example.products.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import java.net.HttpRetryException

open class BaseRepository {
    suspend fun <T : Any> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): Result<T> {
        return withContext(dispatcher) {
            try {
              return@withContext Result.Success(apiCall.invoke())
            } catch (throwable: Exception) {
                when (throwable) {
                    is IOException -> return@withContext Result.NetworkError
                    is HttpRetryException -> {
                        val errorModel = BaseErrorModel(throwable.responseCode(), throwable.message)
                       return@withContext Result.GenericError(errorModel)
                    }
                    else -> {
                      return@withContext  Result.GenericError(null)
                    }
                }
            }
        }

    }
}
