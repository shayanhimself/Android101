package com.shayan.android101.datamodel

class Product(
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

class Rating(
    val rate: Float,
    val count: Int
)
