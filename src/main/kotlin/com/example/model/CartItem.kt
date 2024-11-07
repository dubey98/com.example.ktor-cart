package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val itemId:Int,
    var name:String,
    var quantity:Int,
    var price:Double
)
