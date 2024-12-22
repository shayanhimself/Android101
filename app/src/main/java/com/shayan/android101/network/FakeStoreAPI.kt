package com.shayan.android101.network

import com.shayan.android101.datamodel.Product
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreAPI {

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product

    companion object {
        private const val BASE_URL = "https://fakestoreapi.com/"

        fun create() : FakeStoreAPI {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val okhttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FakeStoreAPI::class.java)
        }
    }
}
