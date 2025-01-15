package com.shayan.android101.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.android101.datamodel.Product
import com.shayan.android101.network.FakeStoreApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ProductViewModel : ViewModel() {
    private val api = FakeStoreApi()

    val product: StateFlow<Product?> = flow {
        emit(api.getProduct(id = 1))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
}
