package com.example.products.data.api

import com.example.products.data.model.ProductCategory

interface ApiHelper {
   suspend fun fetchProducts() : List<ProductCategory>
}