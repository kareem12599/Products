package com.example.products.util

import com.example.products.base.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test

import org.junit.Assert.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.IllegalStateException

@ExperimentalCoroutinesApi
class NetworkUtilsTest {

    private val dispatcher = TestCoroutineDispatcher()


    @Test
    fun `when lambda returns successfully then it should emit the result as success `() {
        runBlockingTest {
         val lambdaResult = true
         val result = safeApiCall(dispatcher) { lambdaResult}
         assertEquals(Result.Success(lambdaResult), result)

        }
    }
    @Test
    fun `when lambda throws IOException then it should emit the result as NetworkError`(){
        runBlockingTest {
            val result = safeApiCall(dispatcher) { throw IOException()}
            assertEquals(Result.NetworkError, result)

        }
    }
    @Test
    fun `when lambda throws HttpException then it should emit the result as GenericError`(){
        val errorBody = "{\"error\" : [\"Unexpected error \"]}".toResponseBody("application/json".toMediaTypeOrNull())
       runBlockingTest {
           val result = safeApiCall(dispatcher){
               throw HttpException(Response.error<Any>(400, errorBody))
           }
           assertEquals(Result.GenericError("Unexpected error"), result)
       }

    }

    @Test
    fun  `when lambda throws unknown exception then it should emit GenericError`(){
        runBlockingTest {
            val result = safeApiCall(dispatcher){
                throw IllegalStateException()
            }
            assertEquals(Result.GenericError(null), result)

        }

    }
}