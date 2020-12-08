package com.example.products.data.model

data class Products (val id:String,
                     val categoryId:String,
                     val name: String,
                     val url: String,
                     val description: String,
val salePrice: SalePrice)