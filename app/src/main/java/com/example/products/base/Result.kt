package com.example.products.base

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class GenericError(val error: String?) : Result<Nothing>()
    object NetworkError: Result<Nothing>()
    
}