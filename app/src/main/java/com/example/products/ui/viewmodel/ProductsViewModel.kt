package com.example.products.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.products.base.BaseErrorModel
import com.example.products.base.Result
import com.example.products.data.model.ProductCategory
import com.example.products.data.model.Products
import com.example.products.data.repository.ProductsRepository
import kotlinx.coroutines.launch

class ProductsViewModel @ViewModelInject constructor(
    private val repository: ProductsRepository
) :
    ViewModel() {
    private val _products = MutableLiveData<List<Products>>()
    val products: LiveData<List<Products>>
        get() = _products
    init {
        fetchProducts()
    }

    private fun fetchProducts(category: String = DEFAULT_CATEGORY) {
        viewModelScope.launch {
            repository.fetchProducts().let {
            when(it){
                is Result.Success -> showSuccessData(it.data, category)
                is Result.NetworkError -> showNetworkError()
                is Result.GenericError -> handleGenericError(it.error)
            }
        }
            }

    }

    private fun handleGenericError(error: BaseErrorModel?) {

    }

    private fun showNetworkError() {
        TODO("Not yet implemented")
    }

    private fun showSuccessData(data: List<ProductCategory>, category: String) {
        data.forEach {
            if (it.name == category)
                _products.postValue(it.products)
        }
    }

    companion object{
        const val DEFAULT_CATEGORY = "food"
    }
}