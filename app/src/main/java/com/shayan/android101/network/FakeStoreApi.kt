package com.shayan.android101.network

import com.shayan.android101.datamodel.Product
import io.ktor.client.call.body
import io.ktor.client.request.get

class FakeStoreApi {
    private val client = KtorClient.client
    private val baseUrl = "https://shayanhimself.github.io/fake-store-api"

    suspend fun getProduct(id: Int): Product = client.get("$baseUrl/products/$id.json").body()

    suspend fun getAllProducts(): List<Product> {
        return (1..5).map { id -> getProduct(id) }
    }
}
