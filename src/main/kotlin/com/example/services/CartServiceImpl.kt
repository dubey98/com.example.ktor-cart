package com.example.services

import com.example.model.CartItem
import com.example.model.CartItems
import com.example.plugins.UpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import javax.inject.Inject

interface CartService {
    suspend fun getAllItems(): List<CartItem>
    suspend fun addItemsInCart(cartItem: CartItem): Int
    suspend fun removeItemFromCart(itemId: Int): Int
    suspend fun updateItemInCart(itemId: Int, updateRequest: UpdateRequest): Int
}

class CartServiceImpl: CartService {
    override suspend fun getAllItems(): List<CartItem> {
        val cartItems = withContext(Dispatchers.IO) {
            transaction {
                CartItems.selectAll().map {
                    CartItem(
                        id = it[CartItems.id].value,
                        name = it[CartItems.name],
                        quantity = it[CartItems.quantity],
                        price = it[CartItems.price]
                    )
                }
            }
        }
        return cartItems
    }

    override suspend fun addItemsInCart(cartItem: CartItem): Int {
        val newItemId = withContext(Dispatchers.IO) {
            transaction {
                CartItems.insertAndGetId {
                    it[name] = cartItem.name
                    it[price] = cartItem.price
                    it[quantity] = cartItem.quantity
                }.value
            }
        }

        return newItemId
    }

    override suspend fun removeItemFromCart(itemId: Int): Int {
        val deletedRows = withContext(Dispatchers.IO) {
            transaction {
                CartItems.deleteWhere { CartItems.id eq itemId }
            }
        }

        return deletedRows
    }

    override suspend fun updateItemInCart(itemId:Int, updateRequest: UpdateRequest): Int {
        val updatedRows = withContext(Dispatchers.IO) {
            transaction {
                CartItems.update ({ CartItems.id eq itemId}) {
                    updateRequest.name?.let { name -> it[CartItems.name] = name }
                    updateRequest.quantity?.let { quantity -> it[CartItems.quantity]  = quantity }
                    updateRequest.price?.let { price -> it[CartItems.price] = price }
                }
            }
        }
        return updatedRows
    }
}