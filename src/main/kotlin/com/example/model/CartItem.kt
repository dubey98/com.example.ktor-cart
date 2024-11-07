package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val itemId:Int,
    val name:String,
    var quantity:Int,
    val price:Double
)
