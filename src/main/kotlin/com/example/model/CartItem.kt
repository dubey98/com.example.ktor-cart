package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id:Int? = null,
    var name: String,
    var quantity: Int,
    var price: Double
)

object CartItems : IntIdTable() {
    var name = varchar("name", 255)
    var quantity = integer("quantity")
    var price = double("price")
}