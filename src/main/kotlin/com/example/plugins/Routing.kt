package com.example.plugins

import com.example.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


val cart: Cart = Cart()

@Serializable
data class UpdateRequest(
    val quantity: Int? = null,
    val price: Double? = null,
    val name: String? = null
)

fun Application.configureRouting() {
    routing {
        get("/cart") {
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

            call.respond(cartItems)
        }

        post("/cart/add") {
            val cartItem = call.receive<CartItem>()

            val newItemId = withContext(Dispatchers.IO) {
                transaction {
                    CartItems.insertAndGetId {
                        it[name] = cartItem.name
                        it[price] = cartItem.price
                        it[quantity] = cartItem.quantity
                    }.value
                }
            }

            call.respond(HttpStatusCode.Created, "Item created with id $newItemId")
        }

        patch("/cart/update/{id}") {
            val itemId = call.parameters["id"]?.toIntOrNull()
            if (itemId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing ItemId Parameter")
                return@patch
            }

            try {
                val dataToUpdate = call.receive<UpdateRequest>()

                val updatedRows = withContext(Dispatchers.IO) {
                    transaction {
                        CartItems.update ({ CartItems.id eq itemId}) {
                            dataToUpdate.name?.let { name -> it[CartItems.name] = name }
                            dataToUpdate.quantity?.let { quantity -> it[CartItems.quantity]  = quantity }
                            dataToUpdate.price?.let { price -> it[CartItems.price] = price }
                        }
                    }
                }

                if(updatedRows > 0) {
                    call.respond(HttpStatusCode.OK, "Successfully Updated")
                } else {
                    call.respond(HttpStatusCode.NotFound, "This id does not exist")
                }

            } catch (ex: IllegalArgumentException) {
                println(ex)
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/cart/remove/{id}") {
            val itemId = call.parameters["id"]?.toIntOrNull()
            if (itemId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing id to delete item")
                return@delete
            }

            try {
                val deletedRows = withContext(Dispatchers.IO) {
                    transaction {
                        CartItems.deleteWhere { CartItems.id eq itemId }
                    }
                }

                if (deletedRows > 0) {
                    call.respond(HttpStatusCode.OK, "Cart item deleted with id: $itemId")
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
