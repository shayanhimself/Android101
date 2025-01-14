package com.shayan.android101.network

import com.shayan.android101.datamodel.Product
import io.ktor.client.call.body
import io.ktor.client.request.get

class FakeStoreApi {
    private val client = KtorClient.client
    private val baseUrl = "https://fakestoreapi.shayanaryan.com"

    suspend fun getProduct(id: Int): Product = client.get("$baseUrl/products/$id").body()
}
