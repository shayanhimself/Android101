package com.shayan.android101.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shayan.android101.datamodel.Product
import com.shayan.android101.datamodel.Rating

class HomeViewModel : ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    init {
        _product.value = fetchProduct()
    }

    private fun fetchProduct() = Product(
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
