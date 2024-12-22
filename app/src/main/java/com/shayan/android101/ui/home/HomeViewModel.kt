package com.shayan.android101.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.android101.datamodel.Product
import com.shayan.android101.network.FakeStoreAPI
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeViewModel(
    private val api: FakeStoreAPI = FakeStoreAPI.create(),
) : ViewModel() {

    data class ViewState(
        val product: Product? = null,
        val isLoading: Boolean,
        val hasError: Boolean = false
    )

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        _viewState.value = ViewState(isLoading = true)
        fetchProduct()
    }

    private fun fetchProduct() = viewModelScope.launch {
        val id = Random.nextInt(from = 1, until = 20)

        try {
            val product = api.getProduct(id)
            _viewState.value = ViewState(product = product, isLoading = false)
        } catch (e: Exception) {
            _viewState.value = ViewState(hasError = true, isLoading = false)
        }
    }
}
