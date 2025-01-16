package com.shayan.android101.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.android101.datamodel.Product
import com.shayan.android101.network.FakeStoreApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    class ViewState(
        val product: Product? = null,
        val isLoading: Boolean = false,
    )

    private val api = FakeStoreApi()

    val viewState = MutableStateFlow(ViewState(isLoading = true))

    private fun fetchProduct() = viewModelScope.launch {
        val product = api.getProduct(id = 2)
        viewState.value = ViewState(product = product)
    }

    init {
        fetchProduct()
    }
}
