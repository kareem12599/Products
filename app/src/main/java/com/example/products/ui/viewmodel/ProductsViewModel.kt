package com.example.products.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.products.base.Result
import com.example.products.data.model.ProductCategory
import com.example.products.data.model.Products
import com.example.products.data.repository.ProductsRepository
import com.example.products.util.DEFAULT_CATEGORY
import com.example.products.util.NETWORK_ERROR
import kotlinx.coroutines.launch

class ProductsViewModel @ViewModelInject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    private val _products = MutableLiveData<List<Products>>()
    val products: LiveData<List<Products>>
        get() = _products

    private val _categories = MutableLiveData<List<Pair<String, Boolean>>>()
    val categories: LiveData<List<Pair<String, Boolean>>>
        get() = _categories



    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String>
            get() = _errorMessage

    init {
        fetchProducts()
    }

    private fun fetchProducts(category: String = DEFAULT_CATEGORY) {
        _dataLoading.value = true
        viewModelScope.launch {
            repository.fetchProducts().let {
                _dataLoading.value = false
                when (it) {
                    is Result.Success -> showSuccessData(it.data, category)
                    is Result.NetworkError -> showNetworkError()
                    is Result.GenericError -> handleGenericError(it.error)
                }
            }
        }

    }

    private fun handleGenericError(error: String?) {
        error?.let {
            _errorMessage.value  = it
        }
    }

    private fun showNetworkError() {
        _errorMessage.value = NETWORK_ERROR
    }

    private fun showSuccessData(data: List<ProductCategory>, category: String) {
        _categories.postValue(data.map { Pair(it.name , it.name == category) })

        data.forEach {
            if (it.name == category)
                _products.postValue(it.products)

        }
    }

    fun fetchProductsWithCategory(category: String) {
        fetchProducts(category)
    }


    fun refresh(){
        fetchProducts()
    }

}