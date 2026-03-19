package com.shayan.android101.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.android101.datamodel.Product
import com.shayan.android101.network.FakeStoreApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {

    data class ViewState(
        val products: List<Product> = emptyList(),
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
    )

    private val api = FakeStoreApi()

    val viewState = MutableStateFlow(ViewState(isLoading = true))

    private fun fetchProducts() = viewModelScope.launch {
        try {
            val products = api.getAllProducts()
            viewState.value = ViewState(products = products)
        } catch (e: Exception) {
            viewState.value = ViewState(hasError = true)
        }
    }

    init {
        fetchProducts()
    }

    fun onRefresh() {
        viewState.value = ViewState(isLoading = true)
        fetchProducts()
    }
}
