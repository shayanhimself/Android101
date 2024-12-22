package com.shayan.android101.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.android101.datamodel.Product
import com.shayan.android101.datamodel.Rating
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    data class ViewState(
        val product: Product? = null,
        val isLoading: Boolean,
    )

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        _viewState.value = ViewState(isLoading = true)

        viewModelScope.launch {
            val product = fetchProduct()
            _viewState.value = ViewState(product = product, isLoading = false)
        }
    }

    private suspend fun fetchProduct(): Product {
        delay(1000)
        return Product(
            id = 1,
            title = "This Jacket That Makes You Cooler Than the Weather",
            description = "Step into ultimate style and comfort with this versatile jacket. Whether you're braving chilly winds or just pretending it’s cold enough to look this good, this jacket's got you covered—literally. Bonus: It has pockets. Yes, REAL ones. \uD83D\uDD25",
            image = "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg",
            category = "men's clothing",
            price = 9.9f,
            rating = Rating(
                rate = 4.5f,
                count = 340
            ),
        )
    }
}
