package com.shayan.android101.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.android101.datamodel.Product
import com.shayan.android101.network.FakeStoreApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ProductViewModel : ViewModel() {
    private val api = FakeStoreApi()

    class ViewState(
        val product: Product? = null,
        val isLoading: Boolean = true,
        val hasError: Boolean = false,
    )

    val viewState: StateFlow<ViewState> = flow {
        val product = api.getProduct(id = 2)
        emit(
            ViewState(
                product = product,
                isLoading = false,
            )
        )
    }.catch {
        emit(
            ViewState(
                isLoading = false,
                hasError = true,
            )
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, ViewState())
}
