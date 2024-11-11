package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class CartItem @Inject constructor (
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