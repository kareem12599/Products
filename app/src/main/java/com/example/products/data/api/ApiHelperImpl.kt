package com.example.products.data.api

import com.example.products.data.model.ProductCategory
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val service: ApiService) : ApiHelper {
    override suspend fun fetchProducts(): List<ProductCategory> {
        return service.fetchProducts()
    }
}