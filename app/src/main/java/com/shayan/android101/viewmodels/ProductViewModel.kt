package com.shayan.android101.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.android101.datamodel.Product
import com.shayan.android101.network.FakeStoreApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val api = FakeStoreApi()

    val product = MutableStateFlow<Product?>(null)

    private fun fetchProduct() = viewModelScope.launch {
        val response = api.getProduct(id = 1)
        product.value = response
    }

    init {
        fetchProduct()
    }
}
