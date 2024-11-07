package com.example.model

data class Cart (
    val items: MutableList<CartItem> = mutableListOf()
)