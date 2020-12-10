package com.example.products

import com.example.products.data.model.ProductCategory
import com.example.products.data.model.Products
import com.example.products.data.model.SalePrice

 fun getProductsByCategory(categoryName : String) : ProductCategory {
    val product1 = Products("1", "35602", "bread", "/Bread.jpg",
        "", SalePrice("0.81", "Eur")
    )
    val product2 = Products("2", "35602", "Sandwich", "/Sandwich.jpg",
        "", SalePrice("0.21", "Eur")
    )
    val product3 = Products("2", "36803", "Cola", "/Cola.jpg",
        "", SalePrice("0.81", "Eur")
    )
    val products = listOf( product1, product2, product3)
    return ProductCategory("", categoryName , products)
}

fun getProducts() : List<ProductCategory>{
    val productCategory1 = getProductsByCategory("food")
    val productCategory2 = getProductsByCategory("drink")

    return listOf(productCategory1)

}