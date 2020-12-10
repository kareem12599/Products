package com.example.products.data.repository

import com.example.products.MainCoroutineRule
import com.example.products.base.Result
import com.example.products.data.api.ApiHelperImpl
import com.example.products.getProducts
import com.example.products.util.UNEXPECTED_ERROR
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class ProductsRepositoryTest{

    private lateinit var repository: ProductsRepository
    private val apiHelper : ApiHelperImpl = mock()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp(){
        repository = ProductsRepository(apiHelper, Dispatchers.Main)

    }
    @Test
    fun `fetch products when request success returns result success`()  = mainCoroutineRule.runBlockingTest{
        whenever(apiHelper.fetchProducts()).thenReturn(getProducts())

        val result = repository.fetchProducts()
        assertTrue( result is Result.Success)

        result as Result.Success
        assertThat(result, `is`(Result.Success(getProducts())))



    }
    @Test
    fun `fetch products when request failed returns result error`()  = mainCoroutineRule.runBlockingTest{
        val errorBody = "{\"error\" : [\"Unexpected error \"]}".toResponseBody("application/json".toMediaTypeOrNull())
       whenever(apiHelper.fetchProducts()).thenThrow(HttpException(Response.error<Any>(400, errorBody)))
        val result = repository.fetchProducts()
        assertTrue( result is Result.GenericError)
        result as Result.GenericError
        assertThat(result ,`is`(Result.GenericError(UNEXPECTED_ERROR)))
    }
}