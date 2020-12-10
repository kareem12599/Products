package com.example.products.data.api

import com.example.products.data.model.ProductCategory
import retrofit2.http.GET

interface ApiService {
    @GET(".")
    suspend fun fetchProducts(): List<ProductCategory>


}