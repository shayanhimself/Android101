package com.shayan.android101.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.android101.datamodel.Product
import com.shayan.android101.network.FakeStoreApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class ProductViewModel : ViewModel() {

    class ViewState(
        val product: Product? = null,
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
    )

    private val api = FakeStoreApi()

    val viewState = MutableStateFlow(ViewState(isLoading = true))

    private fun fetchProduct() = viewModelScope.launch {
        try {
            val id = Random.nextInt(1, 6)
            val product = api.getProduct(id)
            viewState.value = ViewState(product = product)
        } catch (e: Exception) {
            viewState.value = ViewState(hasError = true)
        }
    }

    init {
        fetchProduct()
    }

    fun onRefresh() {
        viewState.value = ViewState(isLoading = true)
        fetchProduct()
    }
}
