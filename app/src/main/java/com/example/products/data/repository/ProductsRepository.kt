package com.example.products.data.repository

import com.example.products.base.BaseRepository
import com.example.products.base.Result
import com.example.products.data.api.ApiHelper
import com.example.products.data.model.ProductCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val apiHelper: ApiHelper,
     private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : BaseRepository() {
   suspend fun fetchProducts() : Result<List<ProductCategory>>{
     return safeApiCall(dispatcher) {apiHelper.fetchProducts()}

   }

}