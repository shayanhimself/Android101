package com.shayan.android101.datamodel

data class Product(
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Float,
    val count: Int
)
