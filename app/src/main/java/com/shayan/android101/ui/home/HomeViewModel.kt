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
    private val api: FakeStoreAPI = FakeStoreAPI.create()
) : ViewModel() {

    init {
        getProduct()
    }

    private fun getProduct() {
        viewModelScope.launch {
            val id = Random.nextInt(from = 1, until = 20)
            val response = api.getProduct(id)
            _state.value = response.body()
        }
    }

    private val _state = MutableLiveData<Product?>()
    val state: LiveData<Product?> = _state
}
