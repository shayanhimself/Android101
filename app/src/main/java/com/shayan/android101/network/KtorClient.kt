package com.shayan.android101.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson

object KtorClient {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson()
        }
        install(Logging){
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
    }
}
