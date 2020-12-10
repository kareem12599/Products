package com.example.products.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.products.MainCoroutineRule
import com.example.products.base.Result
import com.example.products.data.model.ProductCategory
import com.example.products.data.model.Products
import com.example.products.data.model.SalePrice
import com.example.products.data.repository.ProductsRepository
import com.example.products.getOrAwaitValue
import com.example.products.getProductsByCategory
import com.example.products.observeForTesting
import com.example.products.util.NETWORK_ERROR
import com.example.products.util.UNEXPECTED_ERROR
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ProductsViewModelTest{

    private lateinit var productsViewModel: ProductsViewModel

    private val repository : ProductsRepository = mock()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


   @Before
   fun setUp(){
       productsViewModel = ProductsViewModel(repository)
   }

    @Test
    fun `fetch products when result success, emits live data with products `() = mainCoroutineRule.runBlockingTest{
        whenever(repository.fetchProducts())
            .thenReturn(Result.Success(listOf(getProductsByCategory("food"))))

        productsViewModel.fetchProductsWithCategory("food")

        val products = getProductsByCategory("food").products
        productsViewModel.products.observeForTesting {
            MatcherAssert.assertThat(productsViewModel.products.getOrAwaitValue(), `is`(products)  )
        }


        productsViewModel.categories.observeForTesting {
            MatcherAssert.assertThat(productsViewModel.categories.getOrAwaitValue(), `is`(listOf(Pair("food", true))))
        }

    }

    @Test
    fun `fetch products when result network error, emits live data with error message`() = mainCoroutineRule.runBlockingTest {
       whenever(repository.fetchProducts()).thenReturn(Result.NetworkError)
        productsViewModel.fetchProductsWithCategory("food")
        productsViewModel.errorMessage.observeForTesting {
            MatcherAssert.assertThat(productsViewModel.errorMessage.getOrAwaitValue(), `is`(NETWORK_ERROR)  )
        }
    }

    @Test
    fun `fetch products when result general error, emits live data with common error`() = mainCoroutineRule.runBlockingTest {
        whenever(repository.fetchProducts()).thenReturn(Result.GenericError(UNEXPECTED_ERROR))
        productsViewModel.fetchProductsWithCategory("food")
        productsViewModel.errorMessage.observeForTesting {
            MatcherAssert.assertThat(productsViewModel.errorMessage.getOrAwaitValue(), `is`(UNEXPECTED_ERROR))
        }

    }

    @Test
    fun `fetch products ensure that data loading is set with proper values`() = mainCoroutineRule.runBlockingTest {
        mainCoroutineRule.pauseDispatcher()
        productsViewModel.fetchProductsWithCategory("food")

        productsViewModel.dataLoading.observeForTesting {
            MatcherAssert.assertThat(productsViewModel.dataLoading.getOrAwaitValue(), `is`(true))
        }
        mainCoroutineRule.resumeDispatcher()

        productsViewModel.dataLoading.observeForTesting {
            MatcherAssert.assertThat(productsViewModel.dataLoading.getOrAwaitValue(), `is`(false))
        }

    }
    @Test
    fun `refresh when refresh products data loading then success`(){
        productsViewModel.refresh()
        `fetch products ensure that data loading is set with proper values`()
        `fetch products when result success, emits live data with products `()
    }


}