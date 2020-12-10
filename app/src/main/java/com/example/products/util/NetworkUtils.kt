package com.example.products.util

import com.example.products.base.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.HttpRetryException

suspend fun <T : Any> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): Result<T> {
    return withContext(dispatcher) {
        try {
            return@withContext Result.Success(apiCall.invoke())
        } catch (throwable: Exception) {
            when (throwable) {
                is IOException -> return@withContext Result.NetworkError
                is HttpException -> {
                    return@withContext Result.GenericError(UNEXPECTED_ERROR)
                }
                else -> {
                    return@withContext  Result.GenericError(null)
                }
            }
        }
    }

}